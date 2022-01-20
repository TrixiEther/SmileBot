package smilebot.dao;

public interface AbstractDAO<T> {

    T findById(long id);

    void save(T server);
    void update(T server);
    void delete(T server);

}
