package smilebot.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractDiscordEntity implements ISnowflake {

    @Id
    protected long snowflake;

    public AbstractDiscordEntity() {};

    public AbstractDiscordEntity(long snowflake) {
        this.snowflake = snowflake;
    }

    @Override
    public long getSnowflake() {
        return snowflake;
    }

    @Override
    public void setSnowflake(long snowflake) {
        this.snowflake = snowflake;
    }
}
