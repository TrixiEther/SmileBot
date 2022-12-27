package smilebot.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smilebot.configuration.BotCommand;
import smilebot.configuration.BotCommandItem;
import smilebot.configuration.BotConfigurationProperties;

@Component
public class CommandsManager {

    private static BotConfigurationProperties configuration;

    @Autowired
    public void setConfiguration(BotConfigurationProperties bcp) {
        configuration = bcp;
    }

    public static boolean isInitializationCommand(String command) {
        BotCommandItem item = BotCommandItem.INITIALIZATION;
        return checkCommandName(command, item);
    }

    public static boolean isReinitializationCommand(String command) {
        BotCommandItem item = BotCommandItem.REINITIALIZATION;
        return checkCommandName(command, item);
    }

    public static boolean isStatisticCommand(String command) {
        BotCommandItem item = BotCommandItem.STATISTIC;
        return checkCommandName(command, item);
    }

    private static boolean checkCommandName(String name, BotCommandItem item) {
        for (BotCommand bc : configuration.getCommands()) {
            if (bc.getCommandItem() == item && bc.getCommand().equals(name))
                return true;
        }
        return false;
    }

}
