package smilebot.events;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public abstract class SlashEvent {

    private User userInitiator;
    private MessageChannel channel;

    public SlashEvent(User userInitiator, MessageChannel channel) {
        this.userInitiator = userInitiator;
        this.channel = channel;
    }

    public User getUserInitiator() {
        return userInitiator;
    }

    public void setUserInitiator(User userInitiator) {
        this.userInitiator = userInitiator;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }
}
