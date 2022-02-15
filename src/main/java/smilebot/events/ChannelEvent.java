package smilebot.events;

public abstract class ChannelEvent {

    protected final long snowflake;
    protected final String name;

    public ChannelEvent(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
    }

}
