package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.core.GenericTypeResolver;
import smilebot.utils.HibernateSessionFactoryUtil;

public abstract class AbstractDAOImpl<T> implements AbstractDAO<T> {

    protected final Class<T> genericType;

    @SuppressWarnings("unchecked")
    public AbstractDAOImpl() {
        this.genericType = (Class<T>) GenericTypeResolver
                .resolveTypeArgument(getClass(), AbstractDAOImpl.class);
    }

    public T findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(genericType, id);
    }

    public void save(T entity) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        session.save(entity);
        trx.commit();
        session.close();
    }
    public void update(T entity) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        session.update(entity);
        trx.commit();
        session.close();
    }
    public void delete(T entity) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction trx = session.beginTransaction();
        session.delete(entity);
        trx.commit();
        session.close();
    }
}
