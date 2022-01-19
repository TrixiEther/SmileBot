package smilebot.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements ISnowflake {

    @Id
    @Column(name = "u_snowflake", unique = true)
    private long snowflake;

    private String name;

    @ManyToMany(mappedBy = "users",
            targetEntity = Server.class,
            fetch = FetchType.EAGER
            )
    private Set<Server> servers = new HashSet<>();

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Message> messages;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Reaction> reactions;

    public User() {}

    public User(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
        messages = new ArrayList<>();
        reactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    public void removeServer(Server server) {
        servers.remove(server);
    }

    public Set<Server> getServers() {
        return servers;
    }

    public void setServers(Set<Server> servers) {
        this.servers = servers;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isContainMessage(Message message) {
        return this.messages.contains(message);
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

    @Override
    public String toString() {
        return "model.User{" +
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
