package smilebot.dao;

import smilebot.model.Channel;
import smilebot.model.Emoji;
import smilebot.model.Server;
import smilebot.model.User;

import java.util.List;

public interface ServerDAO {

    Channel findChannelById(int id);
    List<Channel> findAllChannels();

    Emoji findEmojiById(int id);
    List<Emoji> findAllEmoji();

    User findUserById(int id);
    List<User> findAllUsers(Server server);

}
