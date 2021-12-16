package smilebot.model;

public abstract class DiscordEntity implements ISnowflake {

    private long snowflake;

    public long getSnowflake() {
        return this.snowflake;
    }

    public void setSnowflake(long snowflake) {
        this.snowflake = snowflake;
    }
}
