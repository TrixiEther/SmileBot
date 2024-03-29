package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import smilebot.utils.HibernateSessionFactoryUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DiscordDataAccessLayerImpl<T> extends DataAccessLayerImpl<T> implements AbstractDiscordDAO<T> {

    public DiscordDataAccessLayerImpl(Class<T> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    public T findBySnowflake(String s_name, long snowflake) {

        T entity = null;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(genericType);
        Root<T> root = cr.from(genericType);
        cr.select(root).where(cb.equal(root.get(s_name), snowflake));

        Query<T> q = session.createQuery(cr);
        entity = q.uniqueResult();

        return entity;
    }

    public List<T> findMultipleBySnowflake(String s_name, long snowflake) {

        List<T> entities;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(genericType);
        Root<T> root = cr.from(genericType);
        cr.select(root).where(cb.equal(root.get(s_name), snowflake));

        Query<T> q = session.createQuery(cr);
        entities = q.getResultList();

        return entities;
    }

}
