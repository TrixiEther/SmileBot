package smilebot.dao;

import smilebot.model.Channel;
import smilebot.model.Emoji;
import smilebot.model.Server;
import smilebot.model.User;
import smilebot.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ServerDAOImpl extends AbstractDiscordDAOImpl<Server> implements ServerDAO {

    @Override
    public Channel findChannelById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Channel.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Channel> findAllChannels() {
        return (List<Channel>)  HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("From Channel").list();
    }

    @Override
    public Emoji findEmojiById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Emoji.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Emoji> findAllEmoji() {
        return (List<Emoji>)  HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("From Emoji").list();
    }

    @Override
    public User findUserById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User.class, id);
    }

    @Override
    public List<User> findAllUsers(Server server) {
        return null;
    }
}
