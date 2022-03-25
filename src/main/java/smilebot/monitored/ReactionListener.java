package smilebot.monitored;

import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.utils.DiscordEventsPool;

public class ReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent e) {

        System.out.println("Received MessageReactionAddedEvent");
        IDiscordEvent event = DiscordEventFactory.processReactionAddedEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent e) {

        System.out.println("Received MessageReactionRemovedEvent");
        IDiscordEvent event = DiscordEventFactory.processReactionRemovedEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);

    }

    @Override
    public void onMessageReactionRemoveAll(@NotNull MessageReactionRemoveAllEvent e) {

        System.out.println("Received MessageReactionRemovedAllEvent");
        IDiscordEvent event = DiscordEventFactory.processReactionRemovedAllEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);

    }
}
