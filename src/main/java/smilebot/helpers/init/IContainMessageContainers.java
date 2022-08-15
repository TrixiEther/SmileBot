package smilebot.helpers.init;

import java.util.List;

public interface IContainMessageContainers {

    List<MessageContainer> getMessageContainers();
    void addContainer(MessageContainer container);
    MessageContainer findContainer(long snowflake);

}
