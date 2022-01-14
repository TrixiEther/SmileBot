package smilebot.helpers;

import java.util.ArrayList;
import java.util.List;

public class MessageAnalysisResult {

    private List<EmojiCount> inMessageEmojis = new ArrayList<>();

    public void addEmoji(EmojiCount ec) {
        inMessageEmojis.add(ec);
    }

    public List<EmojiCount> getResults() {
        return inMessageEmojis;
    }

}
