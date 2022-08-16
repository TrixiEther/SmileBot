package smilebot.utils;

import smilebot.model.IChannel;
import smilebot.model.IDiscordThread;
import smilebot.model.Message;

public class CachedThread extends AbstractCachedObject implements IDiscordThread {

    private String name;
    private boolean isArchived;

    public CachedThread(long snowflake, String name, boolean isArchived) {
        this.snowflake = snowflake;
        this.name = name;
        this.isArchived = isArchived;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }

    @Override
    public boolean isContainMessage(Message message) {
        return false;
    }

    @Override
    public void addMessage(Message message) {

    }

}
