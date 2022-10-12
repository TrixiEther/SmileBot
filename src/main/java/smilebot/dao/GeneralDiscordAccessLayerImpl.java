package smilebot.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import smilebot.utils.HibernateSessionFactoryUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class GeneralDiscordAccessLayerImpl extends GeneralDiscordAccessLayer {

    public <T,V> Object findByVal(Class<T> clazz, String s_name, V value) {

        Object entity = null;
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(clazz);
        Root<T> root = cr.from(clazz);
        cr.select(root).where(cb.equal(root.get(s_name), value));

        Query<?> q = session.createQuery(cr);
        entity = q.uniqueResult();

        return entity;
    }

    public <T,V> List<T> findMultipleByVal(Class<T> clazz, String column_name, V value) {

        List<T> entities;
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(clazz);
        Root<T> root = cr.from(clazz);
        cr.select(root).where(cb.equal(root.get(column_name), value));

        Query<T> q = session.createQuery(cr);
        entities = q.getResultList();

        return entities;
    }
}
