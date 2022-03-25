package smilebot.events;

public abstract class ReactionEvent implements IDiscordEvent {

    protected long server_snowflake;
    protected long channel_snowflake;
    protected long message_snowflake;
    protected long user_snowflake;
    protected long emoji_snowflake;

    public ReactionEvent(long s_sn, long c_sn, long m_sn, long u_sn, long e_sn) {
        server_snowflake = s_sn;
        channel_snowflake = c_sn;
        message_snowflake = m_sn;
        user_snowflake = u_sn;
        emoji_snowflake = e_sn;
    }

}
