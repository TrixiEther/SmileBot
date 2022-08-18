package smilebot.model;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;

// I spent two days of my life on this...

@Entity
@Subselect("select * from general_summary")
@Immutable
public class GeneralSummary  {

    @EmbeddedId
    private GeneralSummaryPK summaryPK;

    @MapsId("server_sn")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Server.class)
    @JoinColumn(name = "server_sn")
    private Server server;

    @MapsId("emoji_sn")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Emoji.class)
    @JoinColumn(name = "emoji_sn")
    private Emoji emoji;

    public GeneralSummary() {}

    public GeneralSummary(int in_message, int in_reaction){
        this.in_message = in_message;
        this.in_reaction = in_reaction;
    }

    @Column(name = "in_message_result")
    private int in_message;

    @Column(name = "in_reaction_result")
    private int in_reaction;

    public Server getServer() {
        return this.server;
    }

    public Emoji getEmoji() {
        return this.emoji;
    }

    public int getInMessage() {
        return in_message;
    }

    public void setInMessage(int in_message) {
        this.in_message = in_message;
    }

    public int getInReaction() {
        return in_reaction;
    }

    public void setInReaction(int in_reaction) {
        this.in_reaction = in_reaction;
    }

    public int getSummary() {
        return this.in_message + this.in_reaction;
    }

}
