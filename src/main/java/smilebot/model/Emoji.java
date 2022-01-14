package smilebot.model;

import javax.persistence.*;

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

    public Emoji() {};

    public Emoji(long snowflake, String emoji) {
        this.emoji = emoji;
        this.snowflake = snowflake;
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
