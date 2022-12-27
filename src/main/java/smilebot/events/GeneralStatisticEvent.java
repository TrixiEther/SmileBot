package smilebot.events;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import smilebot.service.DiscordService;

public class GeneralStatisticEvent extends SlashEvent implements IDiscordEvent {

    private final long server;

    public GeneralStatisticEvent(long server, User user, MessageChannel channel) {
        super(user, channel);
        this.server = server;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processGetGeneralStatistic(server, getChannel());
    }

}
