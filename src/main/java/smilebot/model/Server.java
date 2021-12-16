package smilebot.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servers")
public class Server extends DiscordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "s_snowflake", unique = true)
    private long snowflake;

    private String name;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Channel> channels;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emoji> emojis;

    public Server() {}

    public Server(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
        channels = new ArrayList<>();
        emojis = new ArrayList<>();
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void addEmoji(Emoji emoji) {
        emojis.add(emoji);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void removeEmoji(Emoji emoji) {
        emojis.remove(emoji);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void addChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public List<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<Emoji> emojis) {
        this.emojis = emojis;
    }

    @Override
    public String toString() {
        return "model.Server{" +
                "id = " + id +
                "snowflake = " + snowflake +
                "name = " + name + "}";
    }

}
