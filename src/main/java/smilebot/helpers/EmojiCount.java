package smilebot.helpers;

public class EmojiCount {

    private long snowflake;
    private int count;

    public EmojiCount(long snowflake, int count) {
        this.snowflake = snowflake;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSnowflake() {
        return snowflake;
    }

    public int getCount() {
        return count;
    }


}
