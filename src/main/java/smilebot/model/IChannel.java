package smilebot.model;

import java.util.List;

public interface IChannel extends ISnowflake {

    String getName();
    void setName(String name);
    boolean isContainMessage(Message message);
    void addMessage(Message message);

    List<? extends IDiscordThread> getThreads();

}
