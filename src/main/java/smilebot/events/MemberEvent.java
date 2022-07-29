package smilebot.events;

public abstract class MemberEvent implements IDiscordEvent {

    protected long server_snowflake;
    protected long user_snowflake;

    public MemberEvent(long server_snowflake, long user_snowflake) {
        this.server_snowflake = server_snowflake;
        this.user_snowflake = user_snowflake;
    }

}
