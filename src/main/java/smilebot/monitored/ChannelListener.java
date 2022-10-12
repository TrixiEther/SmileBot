package smilebot.monitored;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.utils.DiscordEventsPool;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent e) {

        System.out.println("Received ChannelCreatedEvent, type="
                + e.getChannelType().toString());
        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.TEXT) {
            try {
                event = DiscordEventFactory.processChannelCreatedEvent(e);
            } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
                customIdAnnotationNotFoundException.printStackTrace();
            }
        } else if (e.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) {
            try {
                event = DiscordEventFactory.processThreadCreatedEvent(e);
            } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
                customIdAnnotationNotFoundException.printStackTrace();
            }
        }
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent e) {

        System.out.println("Received ChannelDeleteEvent, type="
                + e.getChannelType().toString());

        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.TEXT) {
            try {
                event = DiscordEventFactory.processChannelDeletedEvent(e);
            } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
                customIdAnnotationNotFoundException.printStackTrace();
            }
        } else if (e.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) {
            try {
                event = DiscordEventFactory.processThreadDeletedEvent(e);
            } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
                customIdAnnotationNotFoundException.printStackTrace();
            }
        }
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent e) {

        System.out.println("Received ChannelDeleteEvent, type="
                + e.getChannelType().toString());

        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.TEXT) {
            try {
                event = DiscordEventFactory.processChannelUpdatedEvent(e);
            } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
                customIdAnnotationNotFoundException.printStackTrace();
            }
        } else {
            event = DiscordEventFactory.processThreadUpdatedEvent(e);
        }
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onChannelUpdateArchived(ChannelUpdateArchivedEvent e) {

        System.out.println("Received ChannelUpdateArchivedEvent, type="
                + e.getChannelType().toString());

        IDiscordEvent event = null;
        if (e.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD) {
            event = DiscordEventFactory.processThreadUpdatedEvent(e);
        }
        DiscordEventsPool.getInstance().addEvent(event);
    }

}
