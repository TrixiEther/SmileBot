package smilebot.monitored;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.model.Server;
import smilebot.service.DiscordService;
import smilebot.utils.DiscordEventsPool;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        System.out.println("Received messageReceiveEvent");

        IDiscordEvent event = DiscordEventFactory.processMessageReceivedEvent(e);

        if (event != null) {
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }
}
