package smilebot.events;

import net.dv8tion.jda.api.entities.Message;

public abstract class MessageEvent implements IDiscordEvent {

    protected Message message;

    public MessageEvent(Message message) {
        this.message = message;
    }

}
