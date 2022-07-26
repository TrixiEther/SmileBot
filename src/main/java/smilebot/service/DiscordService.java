package smilebot.service;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import smilebot.dao.*;
import smilebot.exceptions.ServerNotFoundException;
import smilebot.helpers.EmojiCount;
import smilebot.helpers.MessageAnalysisHelper;
import smilebot.helpers.MessageAnalysisResult;
import smilebot.helpers.UserReaction;
import smilebot.model.*;
import smilebot.model.Channel;
import smilebot.model.Emoji;
import smilebot.model.User;
import smilebot.utils.CachedData;
import smilebot.utils.CachedServer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscordService {

    private static final ServerDAOImpl serverDAO = new ServerDAOImpl();
    private static final MessageDAOImpl messageDAO = new MessageDAOImpl();
    private static final ChannelDAOImpl channelDAO = new ChannelDAOImpl();
    private static final UserDAOImpl userDAO = new UserDAOImpl();
    private static final EmojiDAOImpl emojiDAO = new EmojiDAOImpl();

    private static final CachedData cachedData = new CachedData();

    public static boolean isServerExists(String snowflake) {
        Server s = serverDAO
                .findBySnowflake("snowflake", Long.parseLong(snowflake));
        return s != null;
    }

    public static void addServer(Guild guild) {

        Server server = new Server(Long.parseLong(guild.getId()), guild.getName());

        for (TextChannel tc : guild.getTextChannels()) {
            Channel channel = new Channel(Long.parseLong(tc.getId()), tc.getName());
            channel.setServer(server);
            server.addChannel(channel);
            System.out.println("gc.snowflake=" + Long.parseLong(tc.getId()) + "gc.name=" + tc.getName());

            Set<ThreadChannel> threadChannelList = new HashSet<>();
            threadChannelList.addAll(tc.getThreadChannels());
            threadChannelList.addAll(tc.retrieveArchivedPublicThreadChannels().complete());

            for (ThreadChannel thc : threadChannelList) {
                DiscordThread thread = new DiscordThread(
                        Long.parseLong(thc.getId()),
                        thc.getName(),
                        thc.isArchived()
                );
                thread.setChannel(channel);
                channel.addThread(thread);
                System.out.println("gthc.snowflake=" + Long.parseLong(thc.getId()) + "gthc.name=" + thc.getName());
            }

        }
        for (RichCustomEmoji e : guild.getEmojiCache().asList()) {
            Emoji emoji = new Emoji(Long.parseLong(e.getId()), e.getName());
            emoji.setServer(server);
            server.addEmoji(emoji);
            System.out.println("e.snowflake=" + Long.parseLong(e.getId()) + "e.name=" + e.getName());
        }
        for (Member m : guild.getMembers()) {
            User user = new User(Long.parseLong(m.getId()), m.getUser().getName());
            user.addServer(server);
            server.addUser(user);
        }

        System.out.println("Ready to save server + " + server.getName() + " + structure...");
        serverDAO.openSession();
        serverDAO.save(server);
        serverDAO.closeSession();

        System.out.println("Starting message analysis...");
        for (TextChannel tc : guild.getTextChannels()) {
            analysisChannelMessages(tc, server);

            Set<ThreadChannel> threadChannelList = new HashSet<>();
            threadChannelList.addAll(tc.getThreadChannels());
            threadChannelList.addAll(tc.retrieveArchivedPublicThreadChannels().complete());
            for (ThreadChannel tch : threadChannelList) {
                analysisChannelMessages(tch, server);
            }
        }

        System.out.println("Ready to save messages");
        serverDAO.openSession();
        serverDAO.merge(server);
        serverDAO.closeSession();
        System.out.println("Server saved!");

    }

    public static void processNewMessage(Message message) {

        CachedServer cachedServer = null;

        try {
            cachedServer = getCachedServer(message.getGuild().getIdLong());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (cachedServer != null) {

            System.out.println("Found server: " + cachedServer.getName());
            MessageAnalysisHelper mah = new MessageAnalysisHelper(cachedServer.getEmojis());
            smilebot.model.Message entityMessage = analyzeContent(message, cachedServer, mah, null);

            if (entityMessage != null) {

                System.out.println("Ready to save new message");
                messageDAO.openSession();
                messageDAO.merge(entityMessage);
                messageDAO.closeSession();
            }

        } else {
            System.out.println("Error, server not found after loading, nothing to do...");
        }

    }

    public static void processDeleteMessage(long snowflake) {

        messageDAO.openSession();
        smilebot.model.Message entityMessage = (smilebot.model.Message) messageDAO.findById(snowflake);
        if (entityMessage != null) {
            messageDAO.delete(entityMessage);
        }
        messageDAO.closeSession();

    }

    public static void processUpdateMessage(long snowflake, Message message) {

        System.out.println("Message has been updated, processing...");

        messageDAO.openSession();
        smilebot.model.Message entityMessage = (smilebot.model.Message) messageDAO.findById(snowflake);

        CachedServer cachedServer = null;
        try {
            cachedServer = getCachedServer(message.getGuild().getIdLong());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            messageDAO.closeSession();
        }

        if (cachedServer != null) {

            if (entityMessage != null) {

                System.out.println("Message found in the database...");

                entityMessage.removeAllEmojiInMessageResults();
                messageDAO.merge(entityMessage);

                MessageAnalysisHelper mah = new MessageAnalysisHelper(cachedServer.getEmojis());
                analyzeContent(message, cachedServer, mah, entityMessage);

                if (entityMessage.getEmojiInMessageResults().size() == 0 && entityMessage.getReactions().size() == 0) {
                    System.out.println("No emoji, no reactions for now, delete...");
                    messageDAO.delete(entityMessage);
                } else {
                    System.out.println("Updating...");
                    messageDAO.merge(entityMessage);
                }

            } else {

                System.out.println("Message not found in the database...");

                entityMessage = new smilebot.model.Message(
                        message.getIdLong(),
                        new User(message.getAuthor().getIdLong(), message.getAuthor().getName()),
                        new Channel(message.getChannel().getIdLong(), message.getChannel().getName()),
                        null
                );

                MessageAnalysisHelper mah = new MessageAnalysisHelper(cachedServer.getEmojis());
                analyzeContent(message, cachedServer, mah, entityMessage);

                if (entityMessage.getEmojiInMessageResults().size() != 0 || entityMessage.getReactions().size() != 0) {
                    System.out.println("The message now contains at least one emoji, so I'll save it");
                    messageDAO.merge(entityMessage);
                } else {
                    System.out.println("The message did not contain emojis after editing, nothing to do...");
                }

            }

        }

        messageDAO.closeSession();

    }

    public static void processChannelCreated(long server_snowflake, String channelName, long channelSnowflake) {

        serverDAO.openSession();

        Server server = (Server) serverDAO.findById(server_snowflake);

        if (server != null) {
            Channel entityChannel = new Channel(channelSnowflake, channelName);
            server.addChannel(entityChannel);
            entityChannel.setServer(server);
            serverDAO.merge(server);
        }

        serverDAO.closeSession();

    }

    public static void processChannelDeleted(long snowflake) {

        channelDAO.openSession();

        Channel channelEntity = (Channel) channelDAO.findById(snowflake);
        if (channelEntity != null) {
            channelDAO.delete(channelEntity);
        }

        channelDAO.closeSession();
    }

    public static void processChannelEdited(long snowflake, String newName) {

        channelDAO.openSession();

        Channel channelEntity = (Channel) channelDAO.findById(snowflake);
        if (channelEntity != null) {
            if (!channelEntity.getName().equals(newName)) {
                channelEntity.setName(newName);
                channelDAO.update(channelEntity);
            }
        }

        channelDAO.closeSession();
    }

    public static void processMessageReactionAdded(long server_sn, long channel_sn, long message_sn, long user_sn, long emoji_sn) {

        messageDAO.openSession();

        smilebot.model.Message entityMessage = (smilebot.model.Message)messageDAO.findById(message_sn);

        CachedServer cachedServer = null;
        try {
            cachedServer = getCachedServer(server_sn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            messageDAO.closeSession();
        }

        boolean isMessageCreated = false;

        if (cachedServer != null) {

            userDAO.openSession();
            User entityUser = (User)userDAO.findById(user_sn);

            emojiDAO.openSession();
            Emoji entityEmoji = (Emoji)emojiDAO.findById(emoji_sn);

            if (entityMessage == null) {

                System.out.println("Message not found, so create record...");

                channelDAO.openSession();
                Channel entityChannel = (Channel)channelDAO.findById(channel_sn);

                entityMessage = new smilebot.model.Message(
                        message_sn,
                        entityUser,
                        entityChannel,
                        null
                );

                channelDAO.closeSession();

                isMessageCreated = true;

            }

            if (entityUser != null && entityEmoji != null) {

                Reaction entityReaction = new Reaction(
                        entityMessage,
                        entityUser,
                        entityEmoji
                );

                entityMessage.addReaction(entityReaction);

            }

            if (isMessageCreated)
                messageDAO.save(entityMessage);
            else
                messageDAO.merge(entityMessage);

            userDAO.closeSession();
            emojiDAO.closeSession();
        }

        messageDAO.closeSession();

    }

    public static void processMessageReactionRemoved(long server_sn, long message_sn, long user_sn, long emoji_sn) {

        messageDAO.openSession();

        smilebot.model.Message entityMessage = (smilebot.model.Message)messageDAO.findById(message_sn);

        CachedServer cachedServer = null;
        try {
            cachedServer = getCachedServer(server_sn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            messageDAO.closeSession();
        }

        if (cachedServer != null && entityMessage != null) {
            entityMessage.removeReactionByUserAndEmoji(user_sn, emoji_sn);

            if (entityMessage.getEmojiInMessageResults().size() == 0 && entityMessage.getReactions().size() == 0) {
                messageDAO.delete(entityMessage);
            } else {
                messageDAO.merge(entityMessage);
            }
        }

        messageDAO.closeSession();
    }

    public static void processMessageReactionRemovedAll(long server_sn, long message_sn) {

        messageDAO.openSession();

        smilebot.model.Message entityMessage = (smilebot.model.Message)messageDAO.findById(message_sn);

        CachedServer cachedServer = null;
        try {
            cachedServer = getCachedServer(server_sn);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            messageDAO.closeSession();
        }

        if (cachedServer != null && entityMessage != null) {
            entityMessage.removeAllReactions();

            if (entityMessage.getEmojiInMessageResults().size() == 0 && entityMessage.getReactions().size() == 0) {
                messageDAO.delete(entityMessage);
            } else {
                messageDAO.merge(entityMessage);
            }
        }

        messageDAO.closeSession();

    }

    public static void processThreadCreated(long snowflake, long channel_snowflake, String name) {

        channelDAO.openSession();

        Channel channel = (Channel) channelDAO.findById(channel_snowflake);

        if (channel != null) {
            DiscordThread entityThread = new DiscordThread(snowflake, name, false);
            channel.addThread(entityThread);
            entityThread.setChannel(channel);
            channelDAO.merge(channel);
        }

        channelDAO.closeSession();
    }

    private static void analysisChannelMessages(GuildMessageChannel ch, Server server) {

        if (ch instanceof TextChannel)
            System.out.println("Channel = " + ch.getName());
        else if (ch instanceof ThreadChannel)
            System.out.println("Thread = " + ch.getName());
        else
            System.out.println("Unknown type...");

        MessageAnalysisHelper mah = new MessageAnalysisHelper(server.getEmojis());

        try {
            int count = 0;
            List<Message> tempMessages = new ArrayList<>();
            String lastId = null;
            boolean lastMessageProcessed = false;

            try {
                lastId = ch.getLatestMessageId();
            } catch (IllegalStateException e) {
                System.out.println("Perhaps when the channel is empty, there is nothing to do...");
                return;
            }

            for (int i = 0;;i++) {

                System.out.println("Starting analyze i = " + i);
                tempMessages.addAll(ch.getHistoryBefore(lastId, 100).complete().getRetrievedHistory());

                if (!lastMessageProcessed) {
                    Message lastMessage = ch.retrieveMessageById(lastId).complete();
                    lastMessageProcessed = true;
                    analyzeContent(lastMessage, server, mah, null);
                }

                if (tempMessages.size() > 0) {
                    for (Message m : tempMessages) {
                        analyzeContent(m, server, mah, null);
                        count++;
                    }

                    lastId = tempMessages.get(tempMessages.size() - 1).getId();

                    tempMessages.clear();

                } else {
                    tempMessages.clear();
                    break;
                }

            }
            System.out.println("Complete! count = " + count);
        } catch (Exception e) {
            System.out.println("Error in analysisChannelMessages: " + e.getMessage());
        }

    }

    private static CachedServer getCachedServer(long snowflake) throws ServerNotFoundException {

        if (cachedData.isUninitialized(snowflake)) {
            System.out.println("Server uninitialized");
            return null;
        }

        CachedServer cachedServer = cachedData.getServerBySnowflake(snowflake);

        try {
            if (cachedServer == null) {
                System.out.println("Server " + snowflake
                        + " not found in the cache, loading...");
                serverDAO.openSession();
                Server server = (Server) serverDAO.findById(snowflake);
                cachedData.addServer(server.getSnowflake(),
                        server.getName(),
                        server.getEmojis(),
                        server.getUsers(),
                        server.getChannels()
                );
                serverDAO.closeSession();

                cachedServer = cachedData.getServerBySnowflake(snowflake);

            }
        } catch (NullPointerException e) {
            cachedData.setUninitializedServer(snowflake);
            throw new ServerNotFoundException();
        }

        return cachedServer;
    }

    private static smilebot.model.Message analyzeContent(Message m, IServer server, MessageAnalysisHelper mah, smilebot.model.Message editableMessage) {

        boolean isPublicThreadPost = (m.getChannel().getType() == ChannelType.GUILD_PUBLIC_THREAD);

        //
        // If the user and channel is in the server list,
        // then we will process the message and add it to the statistics
        // If not, then (for now) ignore
        //

        IUser user = server.findUserBySnowflake(m.getAuthor().getIdLong());
        IChannel channel = null;
        IDiscordThread thread = null;

        if (!isPublicThreadPost) {
            channel = server.findChannelBySnowflake(m.getChannel().getIdLong());
        } else {
            channel = server.findChannelBySnowflake(m.getChannel().asThreadChannel().getParentChannel().getIdLong());
            thread = server.findThreadBySnowflake(m.getChannel().getIdLong());
        }

        MessageAnalysisResult mar = mah.analysisMessageContent(m);
        smilebot.model.Message message = null;

        if (mar.getResults().size() != 0 || mar.getUserReactions().size() != 0) {

            if (editableMessage == null) {

                if (!isPublicThreadPost) {
                    message = new smilebot.model.Message(
                            m.getIdLong(),
                            new User(user.getSnowflake(), user.getName()),
                            new Channel(channel.getSnowflake(), channel.getName()),
                            null
                    );
                } else {
                    message = new smilebot.model.Message(
                            m.getIdLong(),
                            new User(user.getSnowflake(), user.getName()),
                            new Channel(channel.getSnowflake(), channel.getName()),
                            new DiscordThread(thread.getSnowflake(), thread.getName(), thread.isArchived())
                    );
                }

            } else {
                message = editableMessage;
            }

            Emoji entityEmoji = null;
            Channel entityChannel = null;
            DiscordThread entityThread = null;
            User entityUser = null;

            for (EmojiCount ec : mar.getResults()) {

                IEmoji emoji = server.findEmojiBySnowflake(ec.getSnowflake());

                if (emoji != null) {

                    if (server instanceof CachedServer) {
                        entityEmoji = new Emoji(emoji.getSnowflake(), emoji.getEmoji());
                        entityChannel = new Channel(channel.getSnowflake(), channel.getName());
                        if (isPublicThreadPost) {
                            entityThread = new DiscordThread(thread.getSnowflake(), thread.getName(), thread.isArchived());
                        }
                        entityUser = new User(user.getSnowflake(), user.getName());
                    } else {
                        entityEmoji = (Emoji)server.findEmojiBySnowflake(emoji.getSnowflake());
                        entityChannel = (Channel)server.findChannelBySnowflake(channel.getSnowflake());
                        if (isPublicThreadPost) {
                            entityThread = (DiscordThread)server.findThreadBySnowflake(thread.getSnowflake());
                        }
                        entityUser = (User)server.findUserBySnowflake(user.getSnowflake());
                    }


                    EmojiInMessageResult eimr = new EmojiInMessageResult(
                            message,
                            entityEmoji,
                            ec.getCount()
                    );

                    entityEmoji.addEmojiInMessageResult(eimr);
                    message.addEmojiInMessageResult(eimr);

                    if (!entityChannel.isContainMessage(message))
                        entityChannel.addMessage(message);

                    if (!entityUser.isContainMessage(message))
                        entityUser.addMessage(message);

                    if (isPublicThreadPost) {
                        if(!entityThread.isContainMessage(message))
                            entityThread.addMessage(message);
                    }

                }
            }

            for (UserReaction ur : mar.getUserReactions()) {

                User reactUser = (User)server.findUserBySnowflake(ur.getUserSnowflake());
                Emoji reactEmoji = (Emoji)server.findEmojiBySnowflake(ur.getEmojiSnowflake());

                Reaction reaction = new Reaction(message, reactUser, reactEmoji);

                reactUser.addReaction(reaction);
                reactEmoji.addReaction(reaction);
                message.addReaction(reaction);

                //
                //  Messages to which users reacted must also be associated with the channel,
                //  and also with the user who left the reaction
                //
                if (channel != null) {
                    if (!channel.isContainMessage(message))
                        channel.addMessage(message);
                }

                if (thread != null) {
                    if (!thread.isContainMessage(message))
                        thread.addMessage(message);
                }

                if (!reactUser.isContainMessage(message))
                    reactUser.addMessage(message);

            }

            return message;

        }

        return null;

    }

}