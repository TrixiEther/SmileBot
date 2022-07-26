package smilebot.events;

import com.sun.xml.bind.v2.model.core.ID;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class DiscordEventFactory {

    public static IDiscordEvent processMessageReceivedEvent(MessageReceivedEvent e) {

        if (e.getMessage().getContentDisplay().equals("!init")) {
            System.out.println("Create InitializationEvent");
            return new InitializationEvent(e.getGuild());
        } else {
            System.out.println("NewMessageEvent");
            return new NewMessageEvent(e.getMessage());
        }

    }

    public static IDiscordEvent processMessageDeleteEvent(MessageDeleteEvent e) {
        return new DeleteMessageEvent(e.getMessageIdLong());
    }

    public static IDiscordEvent processMessageUpdateEvent(MessageUpdateEvent e) {
        return new UpdateMessageEvent(e.getMessageIdLong(), e.getMessage());
    }

    public static IDiscordEvent processChannelCreatedEvent(ChannelCreateEvent e) {
        return new ChannelCreatedEvent(
                e.getChannel().getIdLong(),
                e.getChannel().getName(),
                e.getGuild().getIdLong()
        );
    }

    public static IDiscordEvent processChannelDeleteEvent(ChannelDeleteEvent e) {
        return new ChannelDeletedEvent(
                e.getChannel().getIdLong(),
                e.getChannel().getName()
        );
    }

    public static IDiscordEvent processChannelUpdatedEvent(ChannelUpdateNameEvent e) {
        return new ChannelEditedEvent(
                e.getChannel().getIdLong(),
                e.getNewValue()
        );
    }

    public static IDiscordEvent processReactionAddedEvent(MessageReactionAddEvent e) {
        return new ReactionAddedEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                e.getMessageIdLong(),
                e.getUserIdLong(),
                e.getReaction().getEmoji().asCustom().getIdLong()
        );
    }

    public static IDiscordEvent processReactionRemovedEvent(MessageReactionRemoveEvent e) {
        return new ReactionRemovedEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                e.getMessageIdLong(),
                e.getUserIdLong(),
                e.getReaction().getEmoji().asCustom().getIdLong()
        );
    }

    public static IDiscordEvent processReactionRemovedAllEvent(MessageReactionRemoveAllEvent e) {
        return new ReactionRemovedAllEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                e.getMessageIdLong(),
                0,
                0
        );
    }

    public static IDiscordEvent processThreadCreatedEvent(MessageReceivedEvent e) {
        return new ThreadCreatedEvent(
                e.getMessage().getIdLong(),
                e.getChannel().getIdLong(),
                e.getMessage().getContentDisplay()
        );
    }

}
