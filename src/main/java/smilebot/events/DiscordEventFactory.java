package smilebot.events;

import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import smilebot.utils.DiscordEventsPool;

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

    public static IDiscordEvent processChannelCreatedEvent(TextChannelCreateEvent e) {
        return new ChannelCreatedEvent(
                e.getChannel().getIdLong(),
                e.getChannel().getName(),
                e.getGuild().getIdLong()
        );
    }

    public static IDiscordEvent processChannelDeleteEvent(TextChannelDeleteEvent e) {
        return new ChannelDeletedEvent(
                e.getChannel().getIdLong(),
                e.getChannel().getName()
        );
    }

    public static IDiscordEvent processChannelUpdatedEvent(TextChannelUpdateNameEvent e) {
        return new ChannelEditedEvent(
                e.getChannel().getIdLong(),
                e.getNewName()
        );
    }

}
