package smilebot.service;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Transaction;
import smilebot.dao.MessageDAOImpl;
import smilebot.dao.ServerDAOImpl;
import smilebot.helpers.EmojiCount;
import smilebot.helpers.MessageAnalysisHelper;
import smilebot.helpers.MessageAnalysisResult;
import smilebot.helpers.UserReaction;
import smilebot.model.*;
import smilebot.model.Emoji;
import smilebot.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiscordService {

    private static final ServerDAOImpl serverDAO = new ServerDAOImpl();
    private static final MessageDAOImpl messageDAO = new MessageDAOImpl();

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
        }
        for (Emote e : guild.getEmotes()) {
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

        System.out.println("Starting message analysis...");
        for (TextChannel tc : guild.getTextChannels()) {
            analysisChannelMessages(tc, server);
        }

        System.out.println("Ready to save");
        serverDAO.save(server);
        System.out.println("Server saved!");

    }

    public static void processNewMessage(Message message) {

        Date current = new Date();

        System.out.println("Start: " + current);
        Server server = (Server)serverDAO.findById(message.getGuild().getIdLong());
        System.out.println("End: " + current);

        if (server != null) {
            System.out.println("Found server: " + server.getName());
            MessageAnalysisHelper mah = new MessageAnalysisHelper(server.getEmojis());
            analyzeContent(message, server, mah);
        }

        serverDAO.merge(server);

    }

    private static void analysisChannelMessages(TextChannel tc, Server server) {

        System.out.println("Channel = " + tc.getName());

        MessageAnalysisHelper mah = new MessageAnalysisHelper(server.getEmojis());

        try {
            int count = 0;
            List<Message> tempMessages = new ArrayList<>();
            String lastId = null;
            boolean lastMessageProcessed = false;

            try {
                lastId = tc.getLatestMessageId();
            } catch (IllegalStateException e) {
                System.out.println("Perhaps when the channel is empty, there is nothing to do...");
                return;
            }

            for (int i = 0;;i++) {

                System.out.println("Starting analyze i = " + i);
                tempMessages.addAll(tc.getHistoryBefore(lastId, 100).complete().getRetrievedHistory());

                if (!lastMessageProcessed) {
                    Message lastMessage = tc.retrieveMessageById(lastId).complete();
                    lastMessageProcessed = true;
                    analyzeContent(lastMessage, server, mah);
                }

                if (tempMessages.size() > 0) {
                    for (Message m : tempMessages) {
                        analyzeContent(m, server, mah);
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

    private static void analyzeContent(Message m, Server server, MessageAnalysisHelper mah) {

        MessageAnalysisResult mar = mah.analysisMessageContent(m);

        if (mar.getResults().size() != 0 || mar.getUserReactions().size() != 0) {

            //
            // If the user and channel is in the server list,
            // then we will process the message and add it to the statistics
            // If not, then (for now) ignore
            //

            User entityUser = server.findUserBySnowflake(m.getAuthor().getIdLong());
            Channel entityChannel = server.findChannelBySnowflake(m.getChannel().getIdLong());

            if (entityUser != null && entityChannel != null) {

                smilebot.model.Message message = new smilebot.model.Message(
                        m.getIdLong(),
                        entityUser,
                        entityChannel
                );

                for (EmojiCount ec : mar.getResults()) {

                    Emoji entityEmoji = server.findEmojiBySnowflake(ec.getSnowflake());

                    if (entityEmoji != null) {

                        EmojiInMessageResult eimr = new EmojiInMessageResult(message, entityEmoji, ec.getCount());

                        entityEmoji.addEmojiInMessageResult(eimr);
                        message.addEmojiInMessageResult(eimr);

                        if (!entityChannel.isContainMessage(message))
                            entityChannel.addMessage(message);

                        if (!entityUser.isContainMessage(message))
                            entityUser.addMessage(message);

                    }
                }

                for (UserReaction ur : mar.getUserReactions()) {

                    User reactUser = server.findUserBySnowflake(ur.getUserSnowflake());
                    Emoji reactEmoji = server.findEmojiBySnowflake(ur.getEmojiSnowflake());

                    Reaction reaction = new Reaction(message,reactUser, reactEmoji);

                    reactUser.addReaction(reaction);
                    reactEmoji.addReaction(reaction);
                    message.addReaction(reaction);

                    //
                    //  Messages to which users reacted must also be associated with the channel,
                    //  and also with the user who left the reaction
                    //

                    if (!entityChannel.isContainMessage(message))
                        entityChannel.addMessage(message);

                    if (!reactUser.isContainMessage(message))
                        reactUser.addMessage(message);

                }

            }

        }
    }
}
