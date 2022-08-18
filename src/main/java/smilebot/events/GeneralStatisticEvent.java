package smilebot.events;

import net.dv8tion.jda.api.entities.Message;
import smilebot.service.DiscordService;

public class GeneralStatisticEvent implements IDiscordEvent {

    private final long server;
    private final Message message;

    public GeneralStatisticEvent(long server, Message message) {
        this.server = server;
        this.message = message;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processGetGeneralStatistic(server,message);
    }

}
