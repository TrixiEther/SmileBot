package smilebot.events;

public interface IDiscordEvent {

    void process();
    default boolean isBlockingBypass() {
        return false;
    }

}
