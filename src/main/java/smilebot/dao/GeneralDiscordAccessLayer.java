package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import smilebot.utils.HibernateSessionFactoryUtil;

public abstract class GeneralDiscordAccessLayer implements IDataAccessLayer {

    protected Session session;

    public GeneralDiscordAccessLayer() {}

    public void openSession() {
        session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

    public void closeSession() {
        if (session != null)
            if (session.isOpen()) {
                session.close();
            }
    }

    @Override
    public void save(Object entity) {
        Transaction trx = session.beginTransaction();
        session.save(entity);
        trx.commit();
    }
    @Override
    public void update(Object entity) {
        Transaction trx = session.beginTransaction();
        session.update(entity);
        trx.commit();
    }
    @Override
    public void delete(Object entity) {
        Transaction trx = session.beginTransaction();
        session.delete(entity);
        trx.commit();
    }
    @Override
    public void merge(Object entity) {
        Transaction trx = session.beginTransaction();
        session.merge(entity);
        trx.commit();
    }

}
