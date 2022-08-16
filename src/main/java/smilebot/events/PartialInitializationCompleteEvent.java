package smilebot.events;

import smilebot.utils.DiscordEventsPool;

public class PartialInitializationCompleteEvent implements IDiscordEvent {
    @Override
    public void process() {
        DiscordEventsPool.getInstance().unlock();
    }

    @Override
    public boolean isBlockingBypass() {
        return true;
    }
}
