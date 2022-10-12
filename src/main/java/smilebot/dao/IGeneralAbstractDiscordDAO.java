package smilebot.dao;

public interface IGeneralAbstractDiscordDAO {

    void save(Object entity);
    void update(Object entity);
    void delete(Object entity);
    void merge(Object entity);
}
