package smilebot.dao;

public interface AbstractDAO<T> {

    T findById(int id);

    void save(T server);
    void update(T server);
    void delete(T server);

}
