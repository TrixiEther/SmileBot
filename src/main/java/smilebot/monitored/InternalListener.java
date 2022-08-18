package smilebot.monitored;

import org.jetbrains.annotations.NotNull;
import smilebot.events.PartialInitializationCompleteEvent;
import smilebot.events.PartialInitializationEvent;
import smilebot.events.ReplyGeneralStatisticEvent;
import smilebot.model.GeneralSummary;
import smilebot.utils.DiscordEventsPool;

import javax.annotation.Nonnull;

public class InternalListener extends InternalListenerAdapter {

    @Override
    public void onPartialInitialization(@NotNull PartialInitializationEvent event) {
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onPartialInitializationComplete(@Nonnull PartialInitializationCompleteEvent event) {
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onReplyGeneralStatistic(@Nonnull ReplyGeneralStatisticEvent event) {

        StringBuilder content = new StringBuilder("Results:\n\n");

        for (GeneralSummary gs : event.getSummaries()) {

            StringBuilder contentPart = new StringBuilder();

            contentPart.append(gs.getEmoji().getEmojiPrintableText()).append(" - ");
            contentPart.append("in message(").append(gs.getInMessage()).append("), ");
            contentPart.append("in reaction(").append(gs.getInReaction()).append("), ");
            contentPart.append("summary(").append(gs.getSummary()).append(")\n");

            if (content.length() + contentPart.length() > 2000) {
                event.getMessage().getChannel().sendMessage(content).queue();
                content = new StringBuilder(contentPart);
            } else {
                content.append(contentPart);
            }

        }

        event.getMessage().getChannel().sendMessage(content).queue();

    }

}
