package smilebot.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import smilebot.service.DiscordService;

public class PartialInitializationEvent extends SlashEvent implements IDiscordEvent {

    private Guild guild;

    public PartialInitializationEvent(Guild guild, User user, MessageChannel channel) {
        super(user, channel);
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processPartialInitialization(this);
    }

    @Override
    public boolean isBlockingBypass() {
        return true;
    }
}
