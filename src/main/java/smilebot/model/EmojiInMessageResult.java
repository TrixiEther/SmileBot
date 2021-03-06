package smilebot.model;

import javax.persistence.*;

@Entity
@Table(name = "emoji_in_message")
public class EmojiInMessageResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_sn")
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emoji_sn")
    private Emoji emoji;

    private int count;

    public EmojiInMessageResult() {};

    public EmojiInMessageResult(Message message, Emoji emoji, int count) {
        this.message = message;
        this.emoji = emoji;
        this.count = count;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }
}
