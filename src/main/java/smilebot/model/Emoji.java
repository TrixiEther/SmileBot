package smilebot.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emoji")
public class Emoji implements ISnowflake {

    @Id
    @Column(name = "e_snowflake")
    private long snowflake;

    @Column(name = "content")
    private String emoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_sn")
    private Server server;


    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "emoji", orphanRemoval = true)
    private List<EmojiInMessageResult> emojiInMessageResults;

    public Emoji() {};

    public Emoji(long snowflake, String emoji) {
        this.emoji = emoji;
        this.snowflake = snowflake;
        emojiInMessageResults = new ArrayList<>();
    }

    public String getEmoji() {
        return emoji;
    }

    public String getEmojiText() {
        return ":" + emoji + ":";
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setEmojiInMessageResults(List<EmojiInMessageResult> emojiInMessageResults) {
        this.emojiInMessageResults = emojiInMessageResults;
    }

    public List<EmojiInMessageResult> getEmojiInMessageResults() {
        return emojiInMessageResults;
    }

    public void addEmojiInMessageResult(EmojiInMessageResult emojiInMessageResult) {
        this.emojiInMessageResults.add(emojiInMessageResult);
    }

    @Override
    public String toString() {
        return "model.Emoji{" +
                "snowflake = " + snowflake +
                ", emoji = " + emoji + "}";
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
