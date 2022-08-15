package smilebot.events;

import net.dv8tion.jda.api.entities.Guild;
import smilebot.service.DiscordService;

public class PartialInitializationEvent implements IDiscordEvent {

    private Guild guild;

    public PartialInitializationEvent(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processPartialInitialization(guild);
    }
}
