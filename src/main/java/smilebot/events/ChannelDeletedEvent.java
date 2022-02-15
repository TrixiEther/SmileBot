package smilebot.events;

import smilebot.service.DiscordService;

public class ChannelDeletedEvent extends ChannelEvent implements IDiscordEvent {

    public ChannelDeletedEvent(long snowflake, String name) {
        super(snowflake, name);
    }

    @Override
    public void process() {
        DiscordService.processChannelDeleted(snowflake);
    }
}
