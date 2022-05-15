package smilebot.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "threads")
@AttributeOverride(name = "snowflake", column = @Column(name = "t_snowflake"))
public class DiscordThread extends AbstractDiscordEntity implements IDiscordThread, IMessageContainer {

    private String name;
    private boolean is_archived;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "channel_sn")
    private Channel channel;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public DiscordThread(long snowflake, String name, boolean is_archived) {
        this.snowflake = snowflake;
        this.name = name;
        this.is_archived = is_archived;
        messages = new ArrayList<>();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean isContainMessage(Message message) {
        return this.messages.contains(message);
    }

    @Override
    public void addMessage(Message message) {
        this.messages.add(message);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isArchived() {
        return is_archived;
    }

    @Override
    public String toString() {
        return "model.Thread{" +
                "snowflake = " + snowflake +
                ", name = " + name + "}";
    }

}
