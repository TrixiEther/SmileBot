package smilebot.events;

import smilebot.service.DiscordService;

public class ChannelUpdatedEvent extends ChannelEvent {

    public ChannelUpdatedEvent(long server_snowflake, long snowflake, String name) {
        super(server_snowflake, snowflake, name);
    }

    @Override
    public void process() {
        DiscordService.getInstance().processChannelUpdate(snowflake, name);
    }
}
