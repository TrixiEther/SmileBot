package smilebot.monitored;

import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.events.PartialInitializationEvent;
import smilebot.utils.DiscordEventsPool;

public class InternalListener extends InternalListenerAdapter {

    @Override
    public void onPartialInitialization(@NotNull PartialInitializationEvent event) {
        DiscordEventsPool.getInstance().addEvent(event);
    }

}
