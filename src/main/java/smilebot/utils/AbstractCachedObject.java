package smilebot.utils;

import smilebot.model.ISnowflake;

public abstract class AbstractCachedObject implements ISnowflake {

    protected long snowflake;

    public AbstractCachedObject() {};

    public AbstractCachedObject(long snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public long getSnowflake() {
        return snowflake;
    }

    @Override
    public void setSnowflake(long snowflake) {
        this.snowflake = snowflake;
    }
}
