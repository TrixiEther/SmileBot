package smilebot.model;

public interface IDiscordThread extends ISnowflake {

    String getName();
    boolean isArchived();
    boolean isContainMessage(Message message);
    void addMessage(Message message);
}
