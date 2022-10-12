package smilebot.monitored;

import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.emoji.EmojiRemovedEvent;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.utils.DiscordEventsPool;

import javax.annotation.Nonnull;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        System.out.println("Received GuildJoinEvent");
        IDiscordEvent event = DiscordEventFactory.processUserJoinEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent e) {
        System.out.println("Received GuildLeaveEvent");
        IDiscordEvent event = DiscordEventFactory.processUserLeftEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onEmojiAdded(@Nonnull EmojiAddedEvent e) {
        System.out.println("Received " + e.getClass().getName());
        try {
            IDiscordEvent event = DiscordEventFactory.processEmojiAddedEvent(e);
            DiscordEventsPool.getInstance().addEvent(event);
        } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
            customIdAnnotationNotFoundException.printStackTrace();
        }
    }

    @Override
    public void onEmojiUpdateName(@Nonnull EmojiUpdateNameEvent e) {
        System.out.println("Received " + e.getClass().getName());
        try {
            IDiscordEvent event = DiscordEventFactory.processEmojiUpdatedEvent(e);
            DiscordEventsPool.getInstance().addEvent(event);
        } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
            customIdAnnotationNotFoundException.printStackTrace();
        }
    }

    @Override
    public void onEmojiRemoved(@Nonnull EmojiRemovedEvent e) {
        System.out.println("Received " + e.getClass().getName());
        try {
            IDiscordEvent event = DiscordEventFactory.processEmojiDeleteEvent(e);
            DiscordEventsPool.getInstance().addEvent(event);
        } catch (CustomIdAnnotationNotFoundException customIdAnnotationNotFoundException) {
            customIdAnnotationNotFoundException.printStackTrace();
        }
    }

}
