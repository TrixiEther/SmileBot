package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.core.GenericTypeResolver;
import smilebot.utils.HibernateSessionFactoryUtil;

public abstract class AbstractDAOImpl<T> implements AbstractDAO<T> {

    protected final Class<T> genericType;

    private Session session;

    @SuppressWarnings("unchecked")
    public AbstractDAOImpl() {
        this.genericType = (Class<T>) GenericTypeResolver
                .resolveTypeArgument(getClass(), AbstractDAOImpl.class);
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

    public void save(T entity) {
        Transaction trx = session.beginTransaction();
        session.save(entity);
        trx.commit();
    }
    public void update(T entity) {
        Transaction trx = session.beginTransaction();
        session.update(entity);
        trx.commit();
    }
    public void delete(T entity) {
        Transaction trx = session.beginTransaction();
        session.delete(entity);
        trx.commit();
    }
    public void merge(T entity) {
        Transaction trx = session.beginTransaction();
        session.merge(entity);
        trx.commit();
    }
}
