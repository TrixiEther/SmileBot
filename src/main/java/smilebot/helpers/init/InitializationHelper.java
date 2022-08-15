package smilebot.helpers.init;

import smilebot.model.IChannel;
import smilebot.model.IDiscordThread;
import smilebot.model.IServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InitializationHelper {

    private final UUID uuid;
    private final long snowflake;
    private final List<MessageContainer> containersList;

    public InitializationHelper(IServer server) {

        this.uuid = UUID.randomUUID();
        this.snowflake = server.getSnowflake();
        containersList = new ArrayList<>();
        List<? extends IChannel> channels = server.getChannels();

        for (IChannel ch : channels) {
            MessageContainer c = new MessageContainer(ch.getSnowflake(), null, ContainerType.CHANNEL);
            for (IDiscordThread t : ch.getThreads()) {
                c.addContainer(
                        new MessageContainer(t.getSnowflake(), c, ContainerType.THREAD)
                );
            }
            containersList.add(c);
        }

    }

    public String getUUID() {
        return uuid.toString();
    }

    public long getSnowflake() {
        return snowflake;
    }

    public List<MessageContainer> getContainersList() {
        return containersList;
    }

}
