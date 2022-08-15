package smilebot.events;

import smilebot.service.DiscordService;

public class ReactionAddedEvent extends ReactionEvent {

    public ReactionAddedEvent(long server_snowflake, long channel_snowflake, long message_snowflake, long user_snowflake, long emoji_snowflake) {
        super(server_snowflake, channel_snowflake, message_snowflake, user_snowflake, emoji_snowflake);
    }

    @Override
    public void process() {
        DiscordService.getInstance()
            .processMessageReactionAdded(server_snowflake, channel_snowflake, message_snowflake, user_snowflake, emoji_snowflake);
    }
}
