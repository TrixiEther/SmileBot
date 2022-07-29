package smilebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smilebot.configuration.BotConfigurationProperties;
import smilebot.monitored.ChannelListener;
import smilebot.monitored.MessageListener;
import smilebot.monitored.ReactionListener;
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
                .createLight(configuration.getToken(),
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.EMOJI)
                .build();
        jda.addEventListener(new MessageListener());
        jda.addEventListener(new ChannelListener());
        jda.addEventListener(new ReactionListener());

        System.out.println("Start pool");
        DiscordEventsPool.getInstance().start();

    }

    public static SmileBotManager getInstance() {
        return smileBotManager;
    }

}
