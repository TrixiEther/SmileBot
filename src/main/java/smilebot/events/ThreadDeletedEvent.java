package smilebot.events;

import smilebot.service.DiscordService;

public class ThreadDeletedEvent extends ThreadEvent {

    public ThreadDeletedEvent(long server_snowflake, long snowflake, long channel_snowflake, String name) {
        super(server_snowflake, snowflake, channel_snowflake, name);
    }

    @Override
    public void process() {
        DiscordService.processThreadDeleted(server_snowflake, snowflake);
    }
}
