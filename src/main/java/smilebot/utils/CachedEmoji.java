package smilebot.utils;

import smilebot.model.IEmoji;
import smilebot.model.ISnowflake;

public class CachedEmoji extends AbstractCachedObject implements IEmoji {

    private String emoji;

    public CachedEmoji(long snowflake, String content) {
        super(snowflake);
        this.emoji = content;
    }

    @Override
    public String getEmoji() {
        return emoji;
    }

    @Override
    public String getEmojiPrintableText() { return "<:" + emoji + ":" + snowflake + ">"; }

    @Override
    public String getEmojiText() {
        return ":" + emoji + ":";
    }

    @Override
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

}
