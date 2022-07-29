package smilebot.events;

import smilebot.service.DiscordService;

public class ThreadUpdatedEvent extends ThreadEvent {

    public ThreadUpdatedEvent(long server_snowflake, long snowflake, long channel_snowflake, String name) {
        super(server_snowflake, snowflake, channel_snowflake, name);
    }

    @Override
    public void process() {
        DiscordService.processThreadUpdate(snowflake, name);
    }

}
