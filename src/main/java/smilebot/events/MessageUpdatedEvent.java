package smilebot.events;

import net.dv8tion.jda.api.entities.Message;
import smilebot.service.DiscordService;

public class MessageUpdatedEvent implements IDiscordEvent {

    private final long snowflake;
    private final Message message;

    public MessageUpdatedEvent(long snowflake, Message message) {
        this.snowflake = snowflake;
        this.message =  message;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processUpdateMessage(snowflake, message);
    }
}
