package smilebot.model;

import javax.persistence.*;

@Entity
@Table (name = "channels")
public class Channel extends DiscordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "c_snowflake")
    private long snowflake;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    public Channel() {}

    public Channel(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
    }

    public int getId() {
        return this.id;
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
                "id = " + id +
                ", snowflake = " + snowflake +
                ", name = " + name + "}";
    }
}
