package smilebot.events;

import smilebot.service.DiscordService;

public class DeleteMessageEvent implements IDiscordEvent {

    private long snowflake;

    public DeleteMessageEvent(long snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public void process() {
        DiscordService.processDeleteMessage(snowflake);
    }
}
