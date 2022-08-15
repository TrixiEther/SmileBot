package smilebot.helpers.init;

public interface IContainMessages {

    void setLastProcessedMessageId(long snowflake);
    long getLastProcessedMessageId();
    void setType(ContainerType type);
    void setStatus(ContainerStatus status);
    ContainerType getType();
    ContainerStatus getStatus();
    long getParentSnowflake();
    long getSnowflake();

}
