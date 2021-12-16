package smilebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.stereotype.Component;
import smilebot.monitored.MessageListener;

import javax.security.auth.login.LoginException;

@Component
public class SmileBotManager {

    public static final SmileBotManager smileBotManager = new SmileBotManager();

    public void startSmileBot() throws LoginException {
        JDA jda = JDABuilder
                .createDefault("TOKEN")
                .build();
        jda.addEventListener(new MessageListener());
    }

    public static SmileBotManager getInstance() {
        return smileBotManager;
    }

}
