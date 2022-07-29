package smilebot.model;

public interface IDiscordThread extends ISnowflake {

    void setName(String name);
    String getName();
    boolean isArchived();
    boolean isContainMessage(Message message);
    void addMessage(Message message);

}
