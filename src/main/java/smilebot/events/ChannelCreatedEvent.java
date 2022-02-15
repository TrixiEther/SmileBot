package smilebot.events;

import smilebot.service.DiscordService;

public class ChannelCreatedEvent extends ChannelEvent implements IDiscordEvent {

    private final long server_snowflake;

    @Override
    public void process() {
        DiscordService.processChannelCreated(server_snowflake, name, snowflake);
    }

    public ChannelCreatedEvent(long snowflake, String name, long s_snowflake) {
        super(snowflake, name);
        this.server_snowflake = s_snowflake;
    }

}
