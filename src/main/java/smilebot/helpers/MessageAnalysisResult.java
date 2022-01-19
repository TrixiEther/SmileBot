package smilebot.helpers;

import java.util.ArrayList;
import java.util.List;

public class MessageAnalysisResult {

    private List<EmojiCount> inMessageEmojis;
    private List<UserReaction> userReactions;

    public MessageAnalysisResult() {
        inMessageEmojis = new ArrayList<>();
        userReactions = new ArrayList<>();
    }

    public void addEmoji(EmojiCount ec) {
        inMessageEmojis.add(ec);
    }

    public List<EmojiCount> getResults() {
        return inMessageEmojis;
    }

    public void addReaction(UserReaction userReaction) {
        userReactions.add(userReaction);
    }

    public List<UserReaction> getUserReactions() {
        return userReactions;
    }
}
