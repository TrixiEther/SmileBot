package smilebot.helpers.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageContainer implements IContainMessages, IContainMessageContainers{

    private final long snowflake;
    private MessageContainer parent;
    private long lastProcessedMessageId;
    private final List<MessageContainer> containerList;
    private ContainerType type;
    private ContainerStatus status;

    public MessageContainer(long snowflake, MessageContainer parent, ContainerType type) {
        containerList = new ArrayList<>();
        this.snowflake = snowflake;
        this.parent = parent;
        this.lastProcessedMessageId = 0;
        this.type = type;
        this.status = ContainerStatus.WAITING;
    }

    @Override
    public List<MessageContainer> getMessageContainers() {
        return containerList;
    }

    @Override
    public void addContainer(MessageContainer container) {
        this.containerList.add(container);
    }

    @Override
    public MessageContainer findContainer(long snowflake) {
        return containerList.stream().filter(c -> (c.snowflake == snowflake))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setLastProcessedMessageId(long snowflake) {
        this.lastProcessedMessageId = snowflake;
    }

    @Override
    public long getLastProcessedMessageId() {
        return lastProcessedMessageId;
    }

    @Override
    public void setType(ContainerType type) {
        this.type = type;
    }

    @Override
    public void setStatus(ContainerStatus status) {
        this.status = status;
    }

    @Override
    public ContainerType getType() {
        return type;
    }

    @Override
    public ContainerStatus getStatus() {
        return status;
    }

    @Override
    public long getParentSnowflake() {
        if (parent != null)
            return parent.getSnowflake();
        return 0;
    }

    @Override
    public long getSnowflake() {
        return snowflake;
    }

}
