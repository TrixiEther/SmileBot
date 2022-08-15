package smilebot.monitored;

import smilebot.events.IDiscordEvent;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface IInternalEventListener {

    void onEvent(@Nonnull IDiscordEvent event);

}
