package smilebot.events;

import net.dv8tion.jda.api.entities.Guild;
import smilebot.service.DiscordService;
import smilebot.utils.DiscordEventsPool;

public class InitializationEvent implements IDiscordEvent {

    private final Guild guild;

    public InitializationEvent(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void process() {

        System.out.println("Received initialization command");

        if (!DiscordService.getInstance().isServerExists(guild.getId())) {

            System.out.println("Server does not exist in the database");

            DiscordEventsPool.getInstance().lock();

            DiscordService.getInstance().addServer(guild);

        } else {
            System.out.println("Server already exist");
        }

    }

    @Override
    public boolean isBlockingBypass() {
        return true;
    }

}
