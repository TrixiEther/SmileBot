package smilebot.model;

import org.hibernate.annotations.Cascade;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "channels")
@AttributeOverride(name = "snowflake", column = @Column(name = "c_snowflake"))
public class Channel extends AbstractDiscordEntity implements IChannel, IMessageContainer {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "server_sn")
    private Server server;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscordThread> threads;

    public Channel() {}

    public Channel(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
        messages = new ArrayList<>();
        threads = new ArrayList<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void addThread(DiscordThread thread) {
        this.threads.add(thread);
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public List<DiscordThread> getThreads() {
        return threads;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean isContainMessage(Message message) {
        return this.messages.contains(message);
    }

    @Override
    public String toString() {
        return "model.Channel{" +
                "snowflake = " + snowflake +
                ", name = " + name + "}";
    }

}
