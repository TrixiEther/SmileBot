package smilebot.utils;

import smilebot.model.IChannel;
import smilebot.model.IDiscordThread;
import smilebot.model.Message;

import java.util.ArrayList;
import java.util.List;

public class CachedChannel extends AbstractCachedObject implements IChannel {

    private String name;
    private List<CachedThread> threads;

    public CachedChannel(long snowflake, String name, List<? extends IDiscordThread> threads) {
        super(snowflake);
        this.name = name;
        this.threads = new ArrayList<>();
        for(IDiscordThread dt : threads) {
            this.threads.add(new CachedThread(dt.getSnowflake(), dt.getName(), dt.isArchived()));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isContainMessage(Message message) {
        return false;
    }

    @Override
    public void addMessage(Message message) {
    }

    @Override
    public List<CachedThread> getThreads() {
        return threads;
    }
}


