package smilebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import smilebot.configuration.BotConfigurationProperties;
import smilebot.monitored.MessageListener;

import javax.security.auth.login.LoginException;

@Component
public class SmileBotManager {

    private static BotConfigurationProperties configuration;

    public static final SmileBotManager smileBotManager = new SmileBotManager();

    @Autowired
    public void setConfiguration(BotConfigurationProperties bcp) {
        configuration = bcp;
    }

    public void startSmileBot() throws LoginException {
        JDA jda = JDABuilder
                .createDefault(configuration.getToken())
                .build();
        jda.addEventListener(new MessageListener());
    }

    public static SmileBotManager getInstance() {
        return smileBotManager;
    }

}
