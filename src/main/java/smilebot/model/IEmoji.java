package smilebot.model;

public interface IEmoji extends ISnowflake {

    String getEmoji();
    String getEmojiPrintableText();
    String getEmojiText();
    void setEmoji(String emoji);

}
