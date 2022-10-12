package smilebot.events;

import java.lang.reflect.InvocationTargetException;

public interface IDiscordEvent {

    void process() throws InvocationTargetException, IllegalAccessException, InstantiationException;
    default boolean isBlockingBypass() {
        return false;
    }

}
