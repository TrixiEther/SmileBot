package smilebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smilebot.configuration.BotCommand;
import smilebot.configuration.BotConfigurationProperties;
import smilebot.monitored.*;
import smilebot.service.DiscordService;
import smilebot.utils.DiscordEventsPool;

import javax.security.auth.login.LoginException;
import java.util.List;

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
        jda.addEventListener(new GuildListener());
        jda.addEventListener(new CommandListener());

        System.out.println("Start pool");
        DiscordEventsPool.getInstance().start();

        System.out.println("Supported commands:");

        for (BotCommand c : configuration.getCommands())
            System.out.println(c.getCommand());

        List<Command> commands = jda.retrieveCommands().complete();
        for (BotCommand bc : configuration.getCommands()) {
            Command commandToEdit = null;
            for (Command command : commands) {
                if (bc.getCommand().equals(command.getName())) {
                    commandToEdit = command;
                    break;
                }
            }
            if (commandToEdit != null) {
                commandToEdit.editCommand()
                        .setName(bc.getCommand())
                        .setDescription(bc.getDescription())
                        .queue();
            } else {
                jda.upsertCommand(bc.getCommand(), bc.getDescription()).queue();
            }
        }

        DiscordService.getInstance().subscribeToInternalEvents(
                new InternalListener()
        );

    }

    public static SmileBotManager getInstance() {
        return smileBotManager;
    }

}
