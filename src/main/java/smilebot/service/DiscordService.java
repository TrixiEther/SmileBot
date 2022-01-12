package smilebot.service;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import smilebot.dao.ServerDAOImpl;
import smilebot.dao.UserDAOImpl;
import smilebot.model.Channel;
import smilebot.model.Emoji;
import smilebot.model.Server;
import smilebot.model.User;

public class DiscordService {

    private static final ServerDAOImpl serverDAO = new ServerDAOImpl();

    public static boolean isServerExists(String snowflake) {
        Server s = serverDAO
                .findBySnowflake("snowflake", Long.parseLong(snowflake));
        return s != null;
    }

    public static void addServer(Guild guild) {

        Server server = new Server(Long.parseLong(guild.getId()), guild.getName());

        for (GuildChannel gc : guild.getChannels()) {
            Channel channel = new Channel(Long.parseLong(gc.getId()), gc.getName());
            channel.setServer(server);
            server.addChannel(channel);
            System.out.println("gc.snowflake=" + Long.parseLong(gc.getId()) + "gc.name=" + gc.getName());
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

        serverDAO.save(server);

    }

    public static boolean initServer(Guild guild) {


        return true;
    }

}
