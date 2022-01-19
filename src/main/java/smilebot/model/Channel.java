package smilebot.model;

import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "channels")
public class Channel implements ISnowflake {

    @Id
    @Column(name = "c_snowflake")
    private long snowflake;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "server_sn")
    private Server server;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Channel() {}

    public Channel(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
        messages = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
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

    @Override
    public String toString() {
        return "model.Channel{" +
                "snowflake = " + snowflake +
                ", name = " + name + "}";
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
