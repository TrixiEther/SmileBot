package smilebot.dao;

public interface AbstractDiscordDAO<T> {

    T findBySnowflake(String s_name, long snowflake);

}
