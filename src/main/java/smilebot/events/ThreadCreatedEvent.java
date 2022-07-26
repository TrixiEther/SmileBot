package smilebot.events;

import smilebot.service.DiscordService;

public class ThreadCreatedEvent extends ThreadEvent implements IDiscordEvent {

    @Override
    public void process() {
        DiscordService.processThreadCreated(snowflake, channel_snowflake, name);
    }

    public ThreadCreatedEvent(long snowflake, long c_snowflake, String name) {
        super(snowflake, c_snowflake, name);
    }

}
