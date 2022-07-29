package smilebot.events;

import smilebot.service.DiscordService;

public class ThreadCreatedEvent extends ThreadEvent implements IDiscordEvent {

    @Override
    public void process() {
        DiscordService.processThreadCreated(server_snowflake, snowflake, channel_snowflake, name);
    }

    public ThreadCreatedEvent(long server_snowflake, long snowflake, long c_snowflake, String name) {
        super(server_snowflake, snowflake, c_snowflake, name);
    }

}
