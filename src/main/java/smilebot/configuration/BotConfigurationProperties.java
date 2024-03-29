package smilebot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "discord")
public class BotConfigurationProperties {

    @Autowired
    private Environment env;

    private String token;
    private List<BotCommand> commands;

    public void setToken(String t) {
        if (env.getProperty("TOKEN") == null) {
            System.out.println("Using TOKEN from application.properties");
            token = t;
        }
        else {
            System.out.println("Using TOKEN from env");
            token = env.getProperty("TOKEN");
        }
    }

    public String getToken() {
        return token;
    }

    public void setCommands(List<BotCommand> commands) {
        this.commands = commands;
    }

    public List<BotCommand> getCommands() {
        return commands;
    }

}
