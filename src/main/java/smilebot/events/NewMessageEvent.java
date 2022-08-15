package smilebot.events;

import net.dv8tion.jda.api.entities.Message;
import smilebot.service.DiscordService;

public class NewMessageEvent extends MessageEvent {

    public NewMessageEvent(Message message) {
        super(message);
    }

    @Override
    public void process() {
        System.out.println("Posted: " + message.getContentDisplay());
        DiscordService.getInstance().processNewMessage(message);
    }

}
