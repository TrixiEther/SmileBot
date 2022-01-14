package smilebot.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements ISnowflake {

    @Id
    @Column(name = "u_snowflake", unique = true)
    private long snowflake;

    private String name;

    @Cascade({
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST
    })
    @ManyToMany(mappedBy = "users",
            targetEntity = Server.class,
            fetch = FetchType.EAGER
            )
    private Set<Server> servers = new HashSet<>();

    public User() {}

    public User(long snowflake, String name) {
        this.snowflake = snowflake;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    public void removeServer(Server server) {
        servers.remove(server);
    }

    public Set<Server> getServers() {
        return servers;
    }

    public void setServers(Set<Server> servers) {
        this.servers = servers;
    }

    @Override
    public String toString() {
        return "model.User{" +
                "snowflake = " + snowflake +
                "name = " + name + "}";
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
