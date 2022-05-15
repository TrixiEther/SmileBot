package smilebot.model;

public interface IServer {

    String getName();
    void setName(String name);

    IEmoji findEmojiBySnowflake(long snowflake);
    IUser findUserBySnowflake(long snowflake);
    IChannel findChannelBySnowflake(long snowflake);
    IDiscordThread findThreadBySnowflake(long snowflake);
    IMessageContainer findMessageContainerBySnowflake(long snowflake);

}
