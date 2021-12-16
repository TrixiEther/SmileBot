package smilebot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discord")
public class BotConfigurationProperties {

    private String token;

    public void setToken(String t) {
        token = t;
    }

    public String getToken() {
        return token;
    }

}
