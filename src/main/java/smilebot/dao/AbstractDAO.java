package smilebot.dao;

public interface AbstractDAO<T> {

    T findById(long id);

    void save(Object entity);
    void update(Object entity);
    void delete(Object entity);
    void merge(Object entity);

}
