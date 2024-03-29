package smilebot.utils;

import smilebot.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Server.class);
                configuration.addAnnotatedClass(Channel.class);
                configuration.addAnnotatedClass(DiscordThread.class);
                configuration.addAnnotatedClass(Emoji.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Message.class);
                configuration.addAnnotatedClass(EmojiInMessageResult.class);
                configuration.addAnnotatedClass(Reaction.class);
                configuration.addAnnotatedClass(GeneralSummaryPK.class);
                configuration.addAnnotatedClass(GeneralSummary.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
