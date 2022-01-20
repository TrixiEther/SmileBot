package smilebot.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "servers")
public class Server implements ISnowflake {

    @Id
    @Column(name = "s_snowflake", unique = true)
    private long snowflake;

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
    private Set<User> users = new HashSet<>();

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

    public void addUser(User user) {
        users.add(user);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void removeEmoji(Emoji emoji) {
        emojis.remove(emoji);
    }

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

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

    @Override
    public long getSnowflake() {
        return this.snowflake;
    }

    @Override
    public void setSnowflake(long snowflake) {
        this.snowflake = snowflake;
    }
}
