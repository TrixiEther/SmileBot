package smilebot.helpers;

public class UserReaction {

    private long e_snowflake;
    private long u_snowflake;

    public UserReaction(long e_snowflake, long u_snowflake) {
        this.e_snowflake = e_snowflake;
        this.u_snowflake = u_snowflake;
    }

    public void setEmojiSnowflake(long e_snowflake) {
        this.e_snowflake = e_snowflake;
    }

    public long getEmojiSnowflake() {
        return e_snowflake;
    }

    public void setUserSnowflake(long u_snowflake) {
        this.u_snowflake = u_snowflake;
    }

    public long getUserSnowflake() {
        return u_snowflake;
    }

}
