package smilebot.events;

import smilebot.service.DiscordService;

public class ChannelCreatedEvent extends ChannelEvent {

    @Override
    public void process() {
        DiscordService.processChannelCreated(server_snowflake, name, snowflake);
    }

    public ChannelCreatedEvent(long server_snowflake, long snowflake, String name) {
        super(server_snowflake, snowflake, name);
    }

}
