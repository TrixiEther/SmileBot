package smilebot.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message implements ISnowflake {

    @Id
    @Column(name = "m_snowflake")
    private long snowflake;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_sn")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_sn")
    private User user;


    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "message", orphanRemoval = true)
    private List<EmojiInMessageResult> emojiInMessageResults;

    public Message() {}

    public Message(long snowflake, User user, Channel channel) {
        this.snowflake = snowflake;
        this.user = user;
        this.channel = channel;
        emojiInMessageResults = new ArrayList<>();
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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

    @Override
    public long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public void setSnowflake(long snowflake) {
        this.snowflake = snowflake;
    }

}
