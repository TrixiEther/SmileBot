package smilebot.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import smilebot.service.DiscordService;
import smilebot.utils.DiscordEventsPool;

public class InitializationEvent extends SlashEvent implements IDiscordEvent {

    private final Guild guild;

    public InitializationEvent(Guild guild, User user, MessageChannel channel) {
        super(user, channel);
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public void process() {

        System.out.println("Received initialization command");

        if (!DiscordService.getInstance().isServerExists(guild.getId())) {

            System.out.println("Server does not exist in the database");

            DiscordEventsPool.getInstance().lock();

            DiscordService.getInstance().addServer(this);

        } else {
            System.out.println("Server already exist");
        }

    }

    @Override
    public boolean isBlockingBypass() {
        return true;
    }

}
