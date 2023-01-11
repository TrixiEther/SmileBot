package smilebot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "discord")
public class BotConfigurationProperties {

    @Autowired
    private Environment env;

    private String token;
    private List<BotCommand> commands;

    public void setToken(String t) {
        token = t;
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
