package smilebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smilebot.configuration.BotConfigurationProperties;
import smilebot.monitored.MessageListener;
import smilebot.utils.DiscordEventsPool;

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

        System.out.println("Start pool");
        DiscordEventsPool.getInstance().start();

    }

    public static SmileBotManager getInstance() {
        return smileBotManager;
    }

}
