package smilebot.monitored;

import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.utils.DiscordEventsPool;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onTextChannelCreate(@NotNull TextChannelCreateEvent e) {

        System.out.println("Received ChannelCreatedEvent");
        IDiscordEvent event = DiscordEventFactory.processChannelCreatedEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onTextChannelDelete(@NotNull TextChannelDeleteEvent e) {

        System.out.println("Received ChannelDeleteEvent");
        IDiscordEvent event = DiscordEventFactory.processChannelDeleteEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onTextChannelUpdateName(@NotNull TextChannelUpdateNameEvent e) {

        System.out.println("Received ChannelUpdatedEvent");
        IDiscordEvent event = DiscordEventFactory.processChannelUpdatedEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);

    }

}
