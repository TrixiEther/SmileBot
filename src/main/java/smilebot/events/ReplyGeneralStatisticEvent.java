package smilebot.events;

import net.dv8tion.jda.api.entities.Message;
import smilebot.model.GeneralSummary;

import java.util.List;

public class ReplyGeneralStatisticEvent implements IDiscordEvent {

    private Message message;
    private List<GeneralSummary> summaries;

    public ReplyGeneralStatisticEvent(Message message, List<GeneralSummary> summaries) {
        this.message = message;
        this.summaries = summaries;
    }

    public Message getMessage() {
        return message;
    }

    public List<GeneralSummary> getSummaries() {
        return summaries;
    }

    @Override
    public void process() {

    }
}
