package smilebot.events;

import smilebot.service.DiscordService;

public class ChannelDeletedEvent extends ChannelEvent {

    public ChannelDeletedEvent(long server_snowflake, long snowflake, String name) {
        super(server_snowflake, snowflake, name);
    }

    @Override
    public void process() {
        DiscordService.processChannelDeleted(server_snowflake, snowflake);
    }
}
