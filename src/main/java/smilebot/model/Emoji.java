package smilebot.model;

import javax.persistence.*;

@Entity
@Table(name = "emoji")
public class Emoji extends DiscordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String emoji;

    @Column(name = "e_snowflake")
    private long snowflake;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    public Emoji() {};

    public Emoji(long snowflake, String emoji) {
        this.emoji = emoji;
        this.snowflake = snowflake;
    }

    public int getId() {
        return this.id;
    }

    public String getEmoji() {
        return emoji;
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
                "id = " + id +
                ", snowflake = " + snowflake +
                ", emoji = " + emoji + "}";
    }

}
