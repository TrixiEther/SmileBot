package smilebot.utils;

import smilebot.model.ISnowflake;
import smilebot.model.IUser;

public class CachedUser extends AbstractCachedObject implements IUser {

    private String name;

    public CachedUser(long snowflake, String name) {
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
