package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.core.GenericTypeResolver;
import smilebot.model.AbstractDiscordEntity;
import smilebot.utils.HibernateSessionFactoryUtil;

public abstract class DataAccessLayerImpl<T> implements IDataAccessLayer<T> {

    protected final Class<T> genericType;

    private Session session;

    @SuppressWarnings("unchecked")
    public DataAccessLayerImpl(Class<T> clazz) {
        this.genericType = clazz;
    }

    public void openSession() {
        session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }

    public void closeSession() {
        if (session != null)
            if (session.isOpen()) {
                session.close();
            }
    }

    public T findById(long id) {
        return session.get(genericType, id);
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
