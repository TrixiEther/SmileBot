package smilebot.helpers;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import smilebot.model.IEmoji;

import java.util.List;

public class MessageAnalysisHelper {

    private List<? extends IEmoji> emojis;

    public MessageAnalysisHelper(List<? extends IEmoji> emojis) {
        this.emojis = emojis;
    }

    public MessageAnalysisResult analysisMessageContent(Message message) {

        String text = message.getContentDisplay();
        MessageAnalysisResult mar = new MessageAnalysisResult();

        for (IEmoji e : emojis) {

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
                EmojiUnion eu = mr.getEmoji();
                // Need to check this, because default Discord emoji could throw exception here
                if (eu.getType() == Emoji.Type.CUSTOM) {
                    long emojiSnowflake = mr.getEmoji().asCustom().getIdLong();
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

        }

        return mar;

    }

}
