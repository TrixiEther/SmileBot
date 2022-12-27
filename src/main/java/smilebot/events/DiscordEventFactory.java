package smilebot.events;

import com.sun.tools.javac.util.Pair;
import net.dv8tion.jda.api.entities.ChannelField;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.GenericChannelUpdateEvent;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.emoji.EmojiRemovedEvent;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.model.*;

public class DiscordEventFactory {

    public static IDiscordEvent processMessageReceivedEvent(MessageReceivedEvent e) {

        if (!e.getAuthor().isBot()) {

            /*
            if (e.getMessage().getContentDisplay().equals("!init")) {
                System.out.println("Create InitializationEvent");
                return new InitializationEvent(e.getGuild());
            } else if (e.getMessage().getContentDisplay().equals("!stat")) {
                System.out.println("Create get GeneralStatisticEvent");
                return new GeneralStatisticEvent(e.getGuild().getIdLong(), e.getMessage());
            } else {
                System.out.println("NewMessageEvent");
                return new NewMessageEvent(e.getMessage());
            }*/

            System.out.println("NewMessageEvent");
            return new NewMessageEvent(e.getMessage());
        }

        return null;

    }

    public static IDiscordEvent processInitializationEvent(SlashCommandInteractionEvent e) {
        return new InitializationEvent(e.getGuild(), e.getUser(), e.getMessageChannel());
    }

    public static IDiscordEvent processReinitializationEvent(SlashCommandInteractionEvent e) {
        return new InitializationEvent(e.getGuild(), e.getUser(), e.getMessageChannel());
    }

    public static IDiscordEvent processGeneralStatisticEvent(SlashCommandInteractionEvent e) {
        return new GeneralStatisticEvent(e.getGuild().getIdLong(), e.getUser(), e.getMessageChannel());
    }

    public static IDiscordEvent processMessageDeletedEvent(MessageDeleteEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata();
        return new GeneralEntityDeleteEvent(
                e.getGuild().getIdLong(),
                Message.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getMessageIdLong())
        );
    }

    public static IDiscordEvent processMessageUpdatedEvent(MessageUpdateEvent e) {
        return new MessageUpdatedEvent(e.getMessageIdLong(), e.getMessage());
    }

    public static IDiscordEvent processChannelCreatedEvent(ChannelCreateEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityCreateEvent(
                e.getGuild().getIdLong(),
                Channel.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getChannel().getIdLong()),
                new Pair<>(CustomFields.CHANNEL_NAME, e.getChannel().getName())
        );
    }

    public static IDiscordEvent processChannelDeletedEvent(ChannelDeleteEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityDeleteEvent(
                e.getGuild().getIdLong(),
                Channel.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getChannel().getIdLong())
        );
    }

    public static IDiscordEvent processChannelUpdatedEvent(GenericChannelUpdateEvent ge) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        if (ge.getPropertyIdentifier().equals(ChannelField.NAME.getFieldName())) {
            return new GeneralEntityUpdateEvent(
                    ge.getGuild().getIdLong(),
                    Channel.class,
                    metadata,
                    new Pair<>(CustomFields.ID, ge.getChannel().getIdLong()),
                    new Pair<>(CustomFields.CHANNEL_NAME, ge.getNewValue())
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

    public static IDiscordEvent processThreadCreatedEvent(ChannelCreateEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityCreateEvent(
                ((ThreadChannel)e.getChannel()).getParentChannel().getIdLong(),
                DiscordThread.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getChannel().getIdLong()),
                new Pair<>(CustomFields.THREAD_NAME, e.getChannel().getName()),
                new Pair<>(CustomFields.THREAD_ARCHIVED, false)
        );
    }

    public static IDiscordEvent processThreadUpdatedEvent(GenericChannelUpdateEvent ge) {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        try {
            if (ge.getPropertyIdentifier().equals(ChannelField.NAME.getFieldName())) {
                return new GeneralEntityUpdateEvent(
                        ((ThreadChannel)ge.getChannel()).getParentChannel().getIdLong(),
                        DiscordThread.class,
                        metadata,
                        new Pair<>(CustomFields.ID, ge.getChannel().getIdLong()),
                        new Pair<>(CustomFields.THREAD_NAME, ge.getNewValue()),
                        new Pair<>(CustomFields.THREAD_ARCHIVED, ((ThreadChannel) ge.getChannel()).isArchived())
                );
            }

            if (ge.getPropertyIdentifier().equals(ChannelField.ARCHIVED.getFieldName())) {
                return new GeneralEntityUpdateEvent(
                        ((ThreadChannel)ge.getChannel()).getParentChannel().getIdLong(),
                        DiscordThread.class,
                        metadata,
                        new Pair<>(CustomFields.ID, ge.getChannel().getIdLong()),
                        new Pair<>(CustomFields.THREAD_NAME, ge.getEntity().getName()),
                        new Pair<>(CustomFields.THREAD_ARCHIVED, ge.getNewValue())
                );
            }
        } catch (NullPointerException | CustomIdAnnotationNotFoundException e) {
            System.out.println("NPE");
            return null;
        }

        return null;
    }

    public static IDiscordEvent processThreadDeletedEvent(ChannelDeleteEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityDeleteEvent(
                ((ThreadChannel)e.getChannel()).getParentChannel().getIdLong(),
                DiscordThread.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getChannel().getIdLong())
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

    public static IDiscordEvent processEmojiAddedEvent(EmojiAddedEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityCreateEvent(
                e.getGuild().getIdLong(),
                Emoji.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getEmoji().getIdLong()),
                new Pair<>(CustomFields.EMOJI_NAME, e.getEmoji().getName())
        );
    }

    public static IDiscordEvent processEmojiUpdatedEvent(EmojiUpdateNameEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityUpdateEvent(
                e.getGuild().getIdLong(),
                Emoji.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getEmoji().getIdLong()),
                new Pair<>(CustomFields.EMOJI_NAME, e.getEmoji().getName())
        );
    }

    public static IDiscordEvent processEmojiDeleteEvent(EmojiRemovedEvent e) throws CustomIdAnnotationNotFoundException {
        EventMetadata metadata = new EventMetadata(
                new Pair<>(EventMetadataType.REFRESH_SERVER, true)
        );
        return new GeneralEntityDeleteEvent(
                e.getGuild().getIdLong(),
                Emoji.class,
                metadata,
                new Pair<>(CustomFields.ID, e.getEmoji().getIdLong())
        );
    }

}
