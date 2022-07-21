package smilebot.monitored;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.utils.DiscordEventsPool;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent e) {

        if (e.getChannelType() == ChannelType.TEXT) {
            System.out.println("Received ChannelCreatedEvent");
            IDiscordEvent event = DiscordEventFactory.processChannelCreatedEvent(e);
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent e) {

        if (e.getChannelType() == ChannelType.TEXT) {
            System.out.println("Received ChannelDeleteEvent");
            IDiscordEvent event = DiscordEventFactory.processChannelDeleteEvent(e);
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent e) {

        if (e.getChannelType() == ChannelType.TEXT) {
            System.out.println("Received ChannelUpdatedEvent");
            IDiscordEvent event = DiscordEventFactory.processChannelUpdatedEvent(e);
            DiscordEventsPool.getInstance().addEvent(event);
        }

    }

}
