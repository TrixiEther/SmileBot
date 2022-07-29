package smilebot.events;

public abstract class ThreadEvent extends ChannelEvent {

    protected final long channel_snowflake;

    public ThreadEvent(long server_snowflake, long snowflake, long channel_snowflake, String name) {
        super(server_snowflake, snowflake, name);
        this.channel_snowflake = channel_snowflake;
    }

}
