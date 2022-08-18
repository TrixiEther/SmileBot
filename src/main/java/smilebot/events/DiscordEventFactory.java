package smilebot.events;

import net.dv8tion.jda.api.entities.ChannelField;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.GenericChannelUpdateEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class DiscordEventFactory {

    public static IDiscordEvent processMessageReceivedEvent(MessageReceivedEvent e) {

        if (!e.getAuthor().isBot()) {
            if (e.getMessage().getContentDisplay().equals("!init")) {
                System.out.println("Create InitializationEvent");
                return new InitializationEvent(e.getGuild());
            } else if (e.getMessage().getContentDisplay().equals("!stat")) {
                System.out.println("Create get GeneralStatisticEvent");
                return new GeneralStatisticEvent(e.getGuild().getIdLong(), e.getMessage());
            } else {
                System.out.println("NewMessageEvent");
                return new NewMessageEvent(e.getMessage());
            }
        }

        return null;

    }

    public static IDiscordEvent processMessageDeletedEvent(MessageDeleteEvent e) {
        return new MessageDeletedEvent(e.getMessageIdLong());
    }

    public static IDiscordEvent processMessageUpdatedEvent(MessageUpdateEvent e) {
        return new MessageUpdatedEvent(e.getMessageIdLong(), e.getMessage());
    }

    public static IDiscordEvent processChannelCreatedEvent(ChannelCreateEvent e) {
        return new ChannelCreatedEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                e.getChannel().getName()
        );
    }

    public static IDiscordEvent processChannelDeletedEvent(ChannelDeleteEvent e) {
        return new ChannelDeletedEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                e.getChannel().getName()
        );
    }

    public static IDiscordEvent processChannelUpdatedEvent(GenericChannelUpdateEvent ge) {
        if (ge.getPropertyIdentifier().equals(ChannelField.NAME.getFieldName())) {
            return new ChannelUpdatedEvent(
                    ge.getGuild().getIdLong(),
                    ge.getEntity().getIdLong(),
                    (String) ge.getNewValue()
            );
        }
        return null;
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

    public static IDiscordEvent processThreadCreatedEvent(ChannelCreateEvent e) {
        return new ThreadCreatedEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                ((ThreadChannel)e.getChannel()).getParentChannel().getIdLong(),
                e.getChannel().getName()
        );
    }

    public static IDiscordEvent processThreadUpdatedEvent(GenericChannelUpdateEvent ge) {
        try {


            if (ge.getPropertyIdentifier().equals(ChannelField.NAME.getFieldName())) {
                return new ThreadUpdatedEvent(
                        ge.getGuild().getIdLong(),
                        ge.getEntity().getIdLong(),
                        ((ThreadChannel) ge.getChannel()).getParentChannel().getIdLong(),
                        (String) ge.getNewValue(),
                        ((ThreadChannel) ge.getChannel()).isArchived()
                );
            }

            if (ge.getPropertyIdentifier().equals(ChannelField.ARCHIVED.getFieldName())) {
                return new ThreadUpdatedEvent(
                        ge.getGuild().getIdLong(),
                        ge.getEntity().getIdLong(),
                        ((ThreadChannel) ge.getChannel()).getParentChannel().getIdLong(),
                        ge.getEntity().getName(),
                        (Boolean) ge.getNewValue()
                );
            }
        } catch (NullPointerException e) {
            System.out.println("NPE");
            return null;
        }

        return null;
    }

    public static IDiscordEvent processThreadDeletedEvent(ChannelDeleteEvent e) {
        return new ThreadDeletedEvent(
                e.getGuild().getIdLong(),
                e.getChannel().getIdLong(),
                ((ThreadChannel)e.getChannel()).getParentChannel().getIdLong(),
                e.getChannel().getName()
        );
    }

    public static IDiscordEvent processUserJoinEvent(GuildMemberJoinEvent e) {
        return new UserJoinEvent(
                e.getGuild().getIdLong(),
                e.getUser().getIdLong(),
                e.getUser().getName()
        );
    }

    public static IDiscordEvent processUserLeftEvent(GuildMemberRemoveEvent e) {
        return new UserLeftEvent(
                e.getGuild().getIdLong(),
                e.getUser().getIdLong()
        );
    }

}
