package smilebot.monitored;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import smilebot.model.Server;
import smilebot.service.DiscordService;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        System.out.println(
            "Received: User=" + e.getAuthor().getName() +
            " Message=" + e.getMessage().getContentDisplay()
        );
        /*if (!e.getAuthor().isBot())
            e.getChannel().sendMessage("OK!").queue();*/

        if (e.getMessage().getContentDisplay().equals("!init")) {

            System.out.println("Received initialization command");

            if (!DiscordService.isServerExists(e.getGuild().getId())) {

                System.out.println("Server does not exist in the database");
                DiscordService.addServer(e.getGuild());

            } else {
                System.out.println("Server already exist");
            }

        }
    }
}
