package smilebot.service;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import smilebot.dao.*;
import smilebot.events.*;
import smilebot.exceptions.ServerNotFoundException;
import smilebot.helpers.EmojiCount;
import smilebot.helpers.MessageAnalysisHelper;
import smilebot.helpers.MessageAnalysisResult;
import smilebot.helpers.UserReaction;
import smilebot.helpers.init.*;
import smilebot.model.*;
import smilebot.model.Channel;
import smilebot.model.Emoji;
import smilebot.model.User;
import smilebot.monitored.IInternalEventListener;
import smilebot.monitored.IInternalEventProducer;
import smilebot.utils.CachedData;
import smilebot.utils.CachedServer;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DiscordService implements IInternalEventProducer {

    private static final DiscordService service = new DiscordService();

    private final DiscordDataAccessLayerImpl<Server> serverDAO = new DiscordDataAccessLayerImpl<>(Server.class);
    private final DiscordDataAccessLayerImpl<smilebot.model.Message> messageDAO = new DiscordDataAccessLayerImpl<>(smilebot.model.Message.class);
    private final DiscordDataAccessLayerImpl<Channel> channelDAO = new DiscordDataAccessLayerImpl<>(Channel.class);
    private final DiscordDataAccessLayerImpl<DiscordThread> threadDAO = new DiscordDataAccessLayerImpl<>(DiscordThread.class);
    private final DiscordDataAccessLayerImpl<User> userDAO = new DiscordDataAccessLayerImpl<>(User.class);
    private final DiscordDataAccessLayerImpl<Emoji> emojiDAO = new DiscordDataAccessLayerImpl<>(Emoji.class);
    private final DiscordDataAccessLayerImpl<GeneralSummary> gSummaryDAO = new DiscordDataAccessLayerImpl<>(GeneralSummary.class);

    private final GeneralDiscordAccessLayerImpl generalDAO = new GeneralDiscordAccessLayerImpl();

    private final CachedData cachedData = new CachedData();

    private final Set<InitializationHelper> initHelpers = new HashSet<>();
    private final Set<IInternalEventListener> internalListeners = new HashSet<>();

    public static DiscordService getInstance() {
        return service;
    }

    public boolean isServerExists(String snowflake) {
        Server s = serverDAO
                .findBySnowflake("snowflake", Long.parseLong(snowflake));
        return s != null;
    }

    public void addServer(InitializationEvent event) {

        Server server = new Server(Long.parseLong(event.getGuild().getId()), event.getGuild().getName());

        for (TextChannel tc : event.getGuild().getTextChannels()) {
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
        for (RichCustomEmoji e : event.getGuild().getEmojiCache().asList()) {
            Emoji emoji = new Emoji(Long.parseLong(e.getId()), e.getName());
            emoji.setServer(server);
            server.addEmoji(emoji);
            System.out.println("e.snowflake=" + Long.parseLong(e.getId()) + "e.name=" + e.getName());
        }
        for (Member m : event.getGuild().getMembers()) {
            User user = new User(Long.parseLong(m.getId()), m.getUser().getName());
            user.addServer(server);
            server.addUser(user);
        }

        System.out.println("Ready to save server + " + server.getName() + " + structure...");
        serverDAO.openSession();
        serverDAO.save(server);
        serverDAO.closeSession();

        initHelpers.add(new InitializationHelper(server));
        for(IInternalEventListener listener : internalListeners)
            listener.onEvent(new PartialInitializationEvent(event.getGuild(), event.getUserInitiator(), event.getChannel()));

    }

    public void processPartialInitialization(PartialInitializationEvent event) {

        System.out.println("Processing Partial Initialization event...");

        serverDAO.openSession();
        Server server;
        long snowflake = event.getGuild().getIdLong();

        InitializationHelper ih = initHelpers.stream()
                .filter(i -> i.getSnowflake() == snowflake)
                .findAny()
                .orElse(null);

        boolean newEventRequired = false;

        if (ih != null) {

            server = (Server) serverDAO.findById(snowflake);
            MessageContainer mc = ih.getContainersList()
                    .stream().filter(c -> c.getStatus() != ContainerStatus.READY)
                    .findFirst()
                    .orElse(null);

            if (mc != null) {
                // The channel must be analyzed from the very beginning or continue from any message
                newEventRequired = true;
                if (mc.getStatus() == ContainerStatus.WAITING || mc.getStatus() == ContainerStatus.PROCESSING) {
                    analysisChannelMessages(
                            event.getGuild().getTextChannelById(mc.getSnowflake()),
                            server,
                            mc
                    );
                } else if (mc.getStatus() == ContainerStatus.NESTED_PROCESSING) {
                    // We take a thread that has not yet been analyzed
                    MessageContainer tmc = mc.getMessageContainers()
                            .stream().filter(t -> t.getStatus() != ContainerStatus.READY)
                            .findFirst()
                            .orElse(null);

                    if (tmc != null) {
                        try {
                            Set<ThreadChannel> threadChannelList = new HashSet<>();
                            threadChannelList.addAll(Objects.requireNonNull(event.getGuild().getTextChannelById(tmc.getParentSnowflake())).getThreadChannels());
                            threadChannelList.addAll(Objects.requireNonNull(event.getGuild().getTextChannelById(tmc.getParentSnowflake())).retrieveArchivedPublicThreadChannels().complete());

                            analysisChannelMessages(
                                    threadChannelList.stream().filter(t -> t.getIdLong() == tmc.getSnowflake())
                                            .findAny()
                                            .orElse(null),
                                    server,
                                    tmc
                            );
                        } catch (NullPointerException e) {
                            System.out.println("Null pointer exception!");
                        }

                    }
                    if (mc.getStatus() == ContainerStatus.NESTED_PROCESSING) {
                        if (mc.getMessageContainers().stream().allMatch(t -> t.getStatus() == ContainerStatus.READY)) {
                            mc.setStatus(ContainerStatus.READY);
                        }
                    }

                } else {
                    mc.setStatus(ContainerStatus.READY);
                }
            }

            serverDAO.merge(server);
            serverDAO.closeSession();

            if (newEventRequired) {
                PartialInitializationEvent piEvent = new PartialInitializationEvent(
                        event.getGuild(),
                        event.getUserInitiator(),
                        event.getChannel());
                for (IInternalEventListener listener : internalListeners) {
                    listener.onEvent(piEvent);
                }
            } else {
                PartialInitializationCompleteEvent picEvent = new PartialInitializationCompleteEvent(
                        event.getUserInitiator(),
                        event.getChannel()
                );
                for (IInternalEventListener listener : internalListeners) {
                    listener.onEvent(picEvent);
                }
                System.out.println("Initialization complete!");
            }

        } else {
            System.out.println("Initialization Helper not found, nothing to do...");
        }

    }

    public void processPartialInitializationComplete(PartialInitializationCompleteEvent event) {
        PostEvent postEvent = new PostEvent(null, event.getUserInitiator(), event.getChannel());
        postEvent.addPost("Initialization complete!");
        for (IInternalEventListener listener : internalListeners)
            listener.onEvent(postEvent);
    }

    public void processGeneralCreateEvent(GeneralEntityCreateEvent e) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        generalDAO.openSession();
        Object preservedEntity = generalDAO.findByVal(e.getPreserverClass(), e.getIdColumnName(),e.getPreservedId());

        if (preservedEntity != null) {
            Object newEntity = e.constructEntity();
            e.invokeParentSetter(newEntity, preservedEntity);
            e.invokeChildAdder(preservedEntity, newEntity);
            generalDAO.save(preservedEntity);
        }

        generalDAO.closeSession();

        if (e.isRefreshRequired()) {
            cachedData.setRequiredRefreshServer((long)e.getPreservedId());
        }

    }

    public void processGeneralUpdateEvent(GeneralEntityUpdateEvent e) throws InvocationTargetException, IllegalAccessException {

        generalDAO.openSession();
        Object entity = generalDAO.findByVal(e.getEntityClass(), e.getIdColumnName(),e.getIdValue());

        if (entity != null) {
            e.invokeSetterMethods(entity);
        }

        generalDAO.merge(entity);
        generalDAO.closeSession();

        if (e.isRefreshRequired()) {
            cachedData.setRequiredRefreshServer((long)e.getPreservedId());
        }

    }

    public void processGeneralDeleteEvent(GeneralEntityDeleteEvent e) {

        generalDAO.openSession();
        Object entity = generalDAO.findByVal(e.getEntityClass(), e.getIdColumnName(),e.getIdValue());

        if (entity != null) {
            generalDAO.delete(entity);
        }

        generalDAO.closeSession();

        if (e.isRefreshRequired()) {
            cachedData.setRequiredRefreshServer((long)e.getPreservedId());
        }
    }

    public void processNewMessage(Message message) {

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

    public void processUpdateMessage(long snowflake, Message message) {

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

    public void processMessageReactionAdded(long server_sn, long channel_sn, long message_sn, long user_sn, long emoji_sn) {

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

    public void processMessageReactionRemoved(long server_sn, long message_sn, long user_sn, long emoji_sn) {

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

    public void processMessageReactionRemovedAll(long server_sn, long message_sn) {

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

    public void processUserJoin(long server_snowflake, long user_snowflake, String name) {

        serverDAO.openSession();

        Server server = (Server) serverDAO.findById(server_snowflake);

        if (server != null) {
            User entityUser = new User(user_snowflake, name);
            server.addUser(entityUser);
            entityUser.addServer(server);
            serverDAO.merge(server);
        }

        serverDAO.closeSession();

        cachedData.setRequiredRefreshServer(server_snowflake);
    }

    public void processUserLeft(long server_snowflake, long snowflake) {

        serverDAO.openSession();

        Server server = (Server) serverDAO.findById(server_snowflake);
        if (server != null) {
            server.removeUser(
                    server.findUserBySnowflake(snowflake)
            );
            serverDAO.merge(server);
        }

        serverDAO.closeSession();

        cachedData.setRequiredRefreshServer(server_snowflake);

    }

    public void processGetGeneralStatistic(long server, MessageChannel channel) {

        gSummaryDAO.openSession();
        List<GeneralSummary> summaries = gSummaryDAO.findMultipleBySnowflake("server", server);
        gSummaryDAO.closeSession();

        if (summaries != null) {

            PostEvent postEvent = new PostEvent(null, null, channel);

            StringBuilder content = new StringBuilder("Results:\n\n");
            for (GeneralSummary gs : summaries) {

                StringBuilder contentPart = new StringBuilder();

                contentPart.append(gs.getEmoji().getEmojiPrintableText()).append(" - ");
                contentPart.append("in message(").append(gs.getInMessage()).append("), ");
                contentPart.append("in reaction(").append(gs.getInReaction()).append("), ");
                contentPart.append("summary(").append(gs.getSummary()).append(")\n");

                if (content.length() + contentPart.length() > 2000) {
                    postEvent.addPost(content.toString());
                    content = new StringBuilder(contentPart);
                } else {
                    content.append(contentPart);
                }
            }

            postEvent.addPost(content.toString());

            for (IInternalEventListener listener : internalListeners) {
                listener.onEvent(postEvent);
            }
        }

    }

    public void processPost(PostEvent e) {
        for (IInternalEventListener listener : internalListeners) {
            listener.onEvent(e);
        }
    }

    private void analysisChannelMessages(GuildMessageChannel ch, Server server, IContainMessages container) {

        if (ch instanceof TextChannel)
            System.out.println("Channel = " + ch.getName());
        else if (ch instanceof ThreadChannel)
            System.out.println("Thread = " + ch.getName());
        else
            System.out.println("Unknown type...");

        MessageAnalysisHelper mah = new MessageAnalysisHelper(server.getEmojis());

        int count = 0;
        List<Message> tempMessages = new ArrayList<>();
        long lastId = 0;

        if (container.getLastProcessedMessageId() != 0) {
            lastId = container.getLastProcessedMessageId();
        } else {
            lastId = ch.getLatestMessageIdLong();
        }

        tempMessages.addAll(ch.getHistoryBefore(lastId, 100).complete().getRetrievedHistory());

        boolean willBeLastIteration = false;
        System.out.println("Temp messages size=" + tempMessages.size());
        if (tempMessages.size() < 100) {
            willBeLastIteration = true;
        }

        container.setStatus(ContainerStatus.PROCESSING);

        if (container.getLastProcessedMessageId() == 0) {
            Message lastMessage = ch.retrieveMessageById(lastId).complete();
            analyzeContent(lastMessage, server, mah, null);
        }

        if (tempMessages.size() > 0) {
            for (Message m : tempMessages) {
                analyzeContent(m, server, mah, null);
                count++;
            }

            container.setLastProcessedMessageId(tempMessages.get(tempMessages.size() - 1).getIdLong());
            tempMessages.clear();
        }

        if (willBeLastIteration) {
            if (container.getType() == ContainerType.THREAD) {
                container.setStatus(ContainerStatus.READY);
            } else {
                container.setStatus(ContainerStatus.NESTED_PROCESSING);
            }
        }

        System.out.println("Complete! count = " + count);

    }

    private CachedServer getCachedServer(long snowflake) throws ServerNotFoundException {

        if (cachedData.isUninitialized(snowflake)) {
            System.out.println("Server uninitialized");
            return null;
        }

        CachedServer cachedServer = cachedData.getServerBySnowflake(snowflake);

        try {
            boolean refresh = cachedData.isRequiredRefresh(snowflake);
            if (cachedServer == null || refresh) {
                if (cachedServer == null) {
                    System.out.println("Server " + snowflake
                            + " not found in the cache, loading...");
                }
                if (refresh) {
                    System.out.println("Server " + snowflake
                            + " needs to be updated, loading...");
                }
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

    private smilebot.model.Message analyzeContent(Message m, IServer server, MessageAnalysisHelper mah, smilebot.model.Message editableMessage) {

        if (m.getAuthor().isBot()) {
            return null;
        }

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

    @Override
    public void subscribeToInternalEvents(IInternalEventListener listener) {
        this.internalListeners.add(listener);
    }
}