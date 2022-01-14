package smilebot.service;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import smilebot.dao.ServerDAOImpl;
import smilebot.dao.UserDAOImpl;
import smilebot.helpers.EmojiCount;
import smilebot.helpers.MessageAnalysisHelper;
import smilebot.helpers.MessageAnalysisResult;
import smilebot.model.Channel;
import smilebot.model.Emoji;
import smilebot.model.Server;
import smilebot.model.User;

import java.util.ArrayList;
import java.util.List;

public class DiscordService {

    private static final ServerDAOImpl serverDAO = new ServerDAOImpl();

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

        System.out.println("Ready to save");
        serverDAO.save(server);
        System.out.println("Server saved!");

        System.out.println("Starting message analysis...");
        for (TextChannel tc : guild.getTextChannels()) {
            analysisChannelMessages(tc, server);
        }

    }

    private static void analysisChannelMessages(TextChannel tc, Server server) {

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
                    tempMessages.add(lastMessage);
                    lastMessageProcessed = true;
                }

                if (tempMessages.size() > 0) {
                    for (Message m : tempMessages) {
                        //System.out.println(m.getContentDisplay());
                        MessageAnalysisResult mar = mah.analysisMessageContent(m.getContentDisplay());

                        if (mar.getResults().size() != 0) {
                            System.out.println("Message contain emojis:");
                            for (EmojiCount e : mar.getResults()) {
                                System.out.println("snowflake = " + e.getSnowflake() + ", count = " + e.getCount());
                            }
                        }

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
}
