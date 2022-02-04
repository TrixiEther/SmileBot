package smilebot.monitored;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.utils.DiscordEventsPool;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        System.out.println("Received messageReceiveEvent");

        IDiscordEvent event = DiscordEventFactory.processMessageReceivedEvent(e);

        if (event != null) {
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent e) {

        System.out.println("Received messageDeleteEvent");

        IDiscordEvent event = DiscordEventFactory.processMessageDeleteEvent(e);

        if (event != null) {
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent e) {

        System.out.println("Received messageUpdateEvent");

        IDiscordEvent event = DiscordEventFactory.processMessageUpdateEvent(e);

        if (event != null) {
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }

}
