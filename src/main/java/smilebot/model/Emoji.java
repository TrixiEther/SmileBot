package smilebot.model;

import org.hibernate.annotations.Cascade;
import smilebot.model.annotations.DiscordEntityClass;
import smilebot.model.annotations.DiscordEntityConstructor;
import smilebot.model.annotations.DiscordEntityField;
import smilebot.model.annotations.DiscordEntityMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emoji")
@AttributeOverride(name = "snowflake", column = @Column(name = "e_snowflake"))
@DiscordEntityClass(containedIn = true)
public class Emoji extends AbstractDiscordEntity implements IEmoji {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "server_sn")
    @DiscordEntityField(isParentContainer = true)
    private Server server;

    @Column(name = "content")
    private String emoji;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "emoji", orphanRemoval = true)
    private List<EmojiInMessageResult> emojiInMessageResults;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @OneToMany(mappedBy = "emoji", orphanRemoval = true)
    private List<Reaction> reactions;

    public Emoji() {};

    @DiscordEntityConstructor(arguments = {CustomFields.ID, CustomFields.EMOJI_NAME})
    public Emoji(long snowflake, String emoji) {
        super(snowflake);
        this.emoji = emoji;
        emojiInMessageResults = new ArrayList<>();
        reactions = new ArrayList<>();
    }

    @Override
    public String getEmoji() {
        return emoji;
    }

    @Override
    public String getEmojiPrintableText() {
        return "<:" + emoji + ":" + snowflake + ">";
    }

    @Override
    public String getEmojiText() {
        return ":" + emoji + ":";
    }

    @Override
    @DiscordEntityMethod(setterFor = CustomFields.EMOJI_NAME)
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Server getServer() {
        return server;
    }

    @DiscordEntityMethod(parentContainerSetter = true)
    public void setServer(Server server) {
        this.server = server;
    }

    public void setEmojiInMessageResults(List<EmojiInMessageResult> emojiInMessageResults) {
        this.emojiInMessageResults = emojiInMessageResults;
    }

    public List<EmojiInMessageResult> getEmojiInMessageResults() {
        return emojiInMessageResults;
    }

    public void addEmojiInMessageResult(EmojiInMessageResult emojiInMessageResult) {
        this.emojiInMessageResults.add(emojiInMessageResult);
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
        return "model.Emoji{" +
                "snowflake = " + snowflake +
                ", emoji = " + emoji + "}";
    }

}
