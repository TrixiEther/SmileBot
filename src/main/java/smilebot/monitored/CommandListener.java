package smilebot.monitored;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.helpers.CommandsManager;
import smilebot.utils.DiscordEventsPool;

import javax.annotation.Nonnull;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent e) {

        IDiscordEvent event = null;

        if (CommandsManager.isInitializationCommand(e.getName())) {
            event = DiscordEventFactory.processInitializationEvent(e);
        } else if (CommandsManager.isReinitializationCommand(e.getName())) {
            event = DiscordEventFactory.processReinitializationEvent(e);
        } else if (CommandsManager.isStatisticCommand(e.getName())) {
            event = DiscordEventFactory.processGeneralStatisticEvent(e);
        }

        if (event != null) {
            DiscordEventsPool.getInstance().addEvent(event);
            e.reply("Request accepted for processing").queue();
        }
        else
            e.reply("Command not supported").queue();

    }

}
