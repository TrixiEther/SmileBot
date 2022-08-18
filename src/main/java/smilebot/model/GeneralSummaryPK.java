package smilebot.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Immutable
public class GeneralSummaryPK implements Serializable {

    @Column(name = "server_sn", updatable = false, insertable = false)
    private long server_sn;

    @Column(name = "emoji_sn", updatable = false, insertable = false)
    private long emoji_sn;

    public GeneralSummaryPK(){}

    public GeneralSummaryPK(long server_sn, long emoji_sn){
        this.server_sn = server_sn;
        this.emoji_sn = emoji_sn;
    }

    public long getServerId() {
        return server_sn;
    }

    public void setServerId(long server) {
        this.server_sn = server;
    }

    public long getEmoji() {
        return emoji_sn;
    }

    public void setEmoji(long emoji) {
        this.emoji_sn = emoji;
    }
}
