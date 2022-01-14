package smilebot.model;

import javax.persistence.*;

@Entity
@Table (name = "channels")
public class Channel implements ISnowflake {

    @Id
    @Column(name = "c_snowflake")
    private long snowflake;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_sn")
    private Server server;

    public Channel() {}

    public Channel(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
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
