package smilebot.model;

import smilebot.model.annotations.DiscordEntityField;
import smilebot.model.annotations.DiscordEntityMethod;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractDiscordEntity implements ISnowflake {

    @Id
    @DiscordEntityField(isId = true, fieldName = CustomFields.ID)
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
    @DiscordEntityMethod(setterFor = CustomFields.ID)
    public void setSnowflake(long snowflake) {
        this.snowflake = snowflake;
    }
}
