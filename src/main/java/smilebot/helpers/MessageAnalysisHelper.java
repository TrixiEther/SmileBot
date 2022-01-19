package smilebot.helpers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import smilebot.model.Emoji;

import java.util.List;

public class MessageAnalysisHelper {

    private List<Emoji> emojis;

    public MessageAnalysisHelper(List<Emoji> emojis) {
        this.emojis = emojis;
    }

    public MessageAnalysisResult analysisMessageContent(Message message) {

        String text = message.getContentDisplay();
        MessageAnalysisResult mar = new MessageAnalysisResult();

        for (Emoji e : emojis) {

            if (text.length() != 0) {

                EmojiCount ec = new EmojiCount(e.getSnowflake(), 0);

                for (int i =0;;i++) {
                    if (text.contains(e.getEmojiText())) {
                        ec.setCount(ec.getCount() + 1);
                        text = text.replaceFirst(e.getEmojiText(),"");
                    } else {
                        break;
                    }
                }
                if (ec.getCount() != 0)
                    mar.addEmoji(ec);

            }

            for (MessageReaction mr : message.getReactions()) {

                long emojiSnowflake = mr.getReactionEmote().getEmote().getIdLong();

                // Count only reactions with the custom emoji
                if (emojiSnowflake == e.getSnowflake()) {
                    for (User u : mr.retrieveUsers().complete()) {
                        mar.addReaction(new UserReaction(
                                emojiSnowflake,
                                u.getIdLong()
                        ));
                    }
                }

            }

        }

        return mar;

    }

}
