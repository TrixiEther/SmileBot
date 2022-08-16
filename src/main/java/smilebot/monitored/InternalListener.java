package smilebot.monitored;

import org.jetbrains.annotations.NotNull;
import smilebot.events.PartialInitializationCompleteEvent;
import smilebot.events.PartialInitializationEvent;
import smilebot.utils.DiscordEventsPool;

import javax.annotation.Nonnull;

public class InternalListener extends InternalListenerAdapter {

    @Override
    public void onPartialInitialization(@NotNull PartialInitializationEvent event) {
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onPartialInitializationComplete(@Nonnull PartialInitializationCompleteEvent event) {
        DiscordEventsPool.getInstance().addEvent(event);
    }

}
