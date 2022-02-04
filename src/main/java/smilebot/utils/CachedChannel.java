package smilebot.utils;

import smilebot.model.IChannel;

public class CachedChannel extends AbstractCachedObject implements IChannel {

    private String name;

    public CachedChannel(long snowflake, String name) {
        super(snowflake);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
