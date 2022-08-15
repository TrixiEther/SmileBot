package smilebot.dao;

public interface IDataAccessLayer<T> {

    T findById(long id);

    void save(Object entity);
    void update(Object entity);
    void delete(Object entity);
    void merge(Object entity);

}
