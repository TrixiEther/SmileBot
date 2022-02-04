package smilebot.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "servers")
@AttributeOverride(name = "snowflake", column = @Column(name = "s_snowflake"))
public class Server extends AbstractDiscordEntity implements IServer {

    private String name;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Channel> channels;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Emoji> emojis;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "server_users",
            joinColumns = @JoinColumn(name = "server_sn"),
            inverseJoinColumns = @JoinColumn(name = "user_sn")
    )
    private List<User> users = new ArrayList<>();

    public Server() {}

    public Server(long snowflake, String name) {
        super(snowflake);
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

    public void addUser(User user) {
        users.add(user);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void removeEmoji(Emoji emoji) {
        emojis.remove(emoji);
    }

    @Override
    public Emoji findEmojiBySnowflake(long snowflake) {
        for (Emoji e : emojis) {
            if (e.getSnowflake() == snowflake)
                return e;
        }
        return null;
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void addChannels(List<Channel> channels) {
        this.channels = channels;
    }

    @Override
    public Channel findChannelBySnowflake(long snowflake) {
        for (Channel c : channels) {
            if (c.getSnowflake() == snowflake)
                return c;
        }
        return null;
    }

    public List<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<Emoji> emojis) {
        this.emojis = emojis;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public User findUserBySnowflake(long snowflake) {
        for (User u : users) {
            if (u.getSnowflake() == snowflake)
                return u;
        }
        return null;
    }

    @Override
    public String toString() {
        return "model.Server{" +
                "snowflake = " + snowflake +
                "name = " + name + "}";
    }

}
