package smilebot.events;

public abstract class ChannelEvent implements IDiscordEvent {

    protected final long server_snowflake;
    protected final long snowflake;
    protected final String name;

    public ChannelEvent(long server_snowflake, long snowflake, String name) {
        this.server_snowflake = server_snowflake;
        this.snowflake = snowflake;
        this.name = name;
    }

}
