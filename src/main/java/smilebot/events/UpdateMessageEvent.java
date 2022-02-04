package smilebot.events;

import net.dv8tion.jda.api.entities.Message;
import smilebot.service.DiscordService;

public class UpdateMessageEvent implements IDiscordEvent {

    private long snowflake;
    private Message message;

    public UpdateMessageEvent(long snowflake, Message message) {
        this.snowflake = snowflake;
        this.message =  message;
    }

    @Override
    public void process() {
        DiscordService.processUpdateMessage(snowflake, message);
    }
}
