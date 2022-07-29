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

        System.out.println("Received ChannelCreatedEvent, type="
                + e.getChannelType().toString());

        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.TEXT) {
            event = DiscordEventFactory.processChannelCreatedEvent(e);
        } else if (e.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) {
            event = DiscordEventFactory.processThreadCreatedEvent(e);
        }
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent e) {

        System.out.println("Received ChannelDeleteEvent, type="
                + e.getChannelType().toString());

        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.TEXT) {
            event = DiscordEventFactory.processChannelDeletedEvent(e);
        } else if (e.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) {
            event = DiscordEventFactory.processThreadDeletedEvent(e);
        }
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent e) {

        System.out.println("Received ChannelDeleteEvent, type="
                + e.getChannelType().toString());

        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.TEXT) {
            event = DiscordEventFactory.processChannelUpdatedEvent(e);
        } else {
            event = DiscordEventFactory.processThreadUpdatedEvent(e);
        }
        DiscordEventsPool.getInstance().addEvent(event);
    }

}
