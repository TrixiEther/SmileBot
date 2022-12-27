package smilebot.events;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import smilebot.service.DiscordService;
import smilebot.utils.DiscordEventsPool;

public class PartialInitializationCompleteEvent extends SlashEvent implements IDiscordEvent {
    public PartialInitializationCompleteEvent(User userInitiator, MessageChannel channel) {
        super(userInitiator, channel);
    }

    @Override
    public void process() {
        DiscordService.getInstance().processPartialInitializationComplete(this);
        DiscordEventsPool.getInstance().unlock();
    }

    @Override
    public boolean isBlockingBypass() {
        return true;
    }
}
