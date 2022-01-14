package smilebot.helpers;

import smilebot.model.Emoji;

import java.util.List;

public class MessageAnalysisHelper {

    private List<Emoji> emojis;

    public MessageAnalysisHelper(List<Emoji> emojis) {
        this.emojis = emojis;
    }

    public MessageAnalysisResult analysisMessageContent(String text) {

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
        }

        return mar;

    }

}
