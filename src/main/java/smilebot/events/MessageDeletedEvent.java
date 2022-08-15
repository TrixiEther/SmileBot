package smilebot.events;

import smilebot.service.DiscordService;

public class MessageDeletedEvent implements IDiscordEvent {

    private final long snowflake;

    public MessageDeletedEvent(long snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processDeleteMessage(snowflake);
    }
}
