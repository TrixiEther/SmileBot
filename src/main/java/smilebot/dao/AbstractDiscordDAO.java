package smilebot.dao;

import java.util.List;

public interface AbstractDiscordDAO<T> {

    T findBySnowflake(String s_name, long snowflake);
    List<T> findMultipleBySnowflake(String s_name, long snowflake);

}
