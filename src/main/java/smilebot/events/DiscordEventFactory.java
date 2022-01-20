package smilebot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import smilebot.utils.DiscordEventsPool;

public class DiscordEventFactory {

    public static IDiscordEvent processMessageReceivedEvent(MessageReceivedEvent e) {

        if (e.getMessage().getContentDisplay().equals("!init")) {
            return new InitializationEvent(e.getGuild());
        } else {
            return new NewMessageEvent(e.getMessage());
        }

    }

}
