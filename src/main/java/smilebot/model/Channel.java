package smilebot.model;

import smilebot.model.annotations.DiscordEntityClass;
import smilebot.model.annotations.DiscordEntityConstructor;
import smilebot.model.annotations.DiscordEntityField;
import smilebot.model.annotations.DiscordEntityMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "channels")
@AttributeOverride(name = "snowflake", column = @Column(name = "c_snowflake"))
@DiscordEntityClass(containedIn = true)
public class Channel extends AbstractDiscordEntity implements IChannel, IMessageContainer {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "server_sn")
    @DiscordEntityField(isParentContainer = true)
    private Server server;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiscordThread> threads;

    public Channel() {}

    @DiscordEntityConstructor(arguments = {CustomFields.ID, CustomFields.CHANNEL_NAME})
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
    @DiscordEntityMethod(setterFor = CustomFields.CHANNEL_NAME)
    public void setName(String name) {
        this.name = name;
    }

    public Server getServer() {
        return server;
    }

    @DiscordEntityMethod(parentContainerSetter = true)
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    @DiscordEntityMethod(childContainerAdder = DiscordThread.class)
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
