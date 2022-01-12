package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import smilebot.model.User;
import smilebot.utils.HibernateSessionFactoryUtil;

public class UserDAOImpl extends AbstractDiscordDAOImpl<User> implements UserDAO {

}
