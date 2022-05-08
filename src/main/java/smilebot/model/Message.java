package smilebot.model;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
@AttributeOverride(name = "snowflake", column = @Column(name = "m_snowflake"))
public class Message extends AbstractDiscordEntity {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "channel_sn")
    private Channel channel;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "thread_sn")
    private DiscordThread thread;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_sn")
    private User user;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "message", orphanRemoval = true)
    private List<EmojiInMessageResult> emojiInMessageResults;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "message", orphanRemoval = true)
    private List<Reaction> reactions;

    public Message() {}

    public Message(long snowflake, User user, Channel channel, DiscordThread thread) {
        this.snowflake = snowflake;
        this.user = user;
        this.channel = channel;
        this.thread = thread;
        emojiInMessageResults = new ArrayList<>();
        reactions = new ArrayList<>();
    }

    public Channel getChannel() {
        return this.channel;
    }

    public DiscordThread getThread() {
        return this.thread;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setThread(DiscordThread thread) {
        this.thread = thread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<EmojiInMessageResult> getEmojiInMessageResults() {
        return emojiInMessageResults;
    }

    public void setEmojiInMessageResults(List<EmojiInMessageResult> emojiInMessageResults) {
        this.emojiInMessageResults = emojiInMessageResults;
    }

    public void addEmojiInMessageResult(EmojiInMessageResult emojiInMessageResult) {
        this.emojiInMessageResults.add(emojiInMessageResult);
    }

    public void removeAllEmojiInMessageResults() {
        this.emojiInMessageResults.clear();
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public void addReaction(Reaction reaction) {
        this.reactions.add(reaction);
    }

    public void removeAllReactions() {
        this.reactions.clear();
    }

    public void removeReactionByUserAndEmoji(long user_sn, long emoji_sn) {

        Reaction reactionToRemove = null;

        for (Reaction r : reactions) {
            if (r.getUser().getSnowflake() == user_sn && r.getEmoji().getSnowflake() == emoji_sn) {
                reactionToRemove = r;
                break;
            }
        }

        if (reactionToRemove != null) {
            reactions.remove(reactionToRemove);
        }

    }

}
