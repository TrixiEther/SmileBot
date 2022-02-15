package smilebot.events;

import smilebot.service.DiscordService;

public class ChannelEditedEvent extends ChannelEvent implements IDiscordEvent {

    public ChannelEditedEvent(long snowflake, String name) {
        super(snowflake, name);
    }

    @Override
    public void process() {
        DiscordService.processChannelEdited(snowflake, name);
    }
}
