package smilebot.monitored;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import smilebot.events.DiscordEventFactory;
import smilebot.events.IDiscordEvent;
import smilebot.utils.DiscordEventsPool;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        System.out.println("Received GuildJoinEvent");
        IDiscordEvent event = DiscordEventFactory.processUserJoinEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent e) {
        System.out.println("Received GuildLeaveEvent");
        IDiscordEvent event = DiscordEventFactory.processUserLeftEvent(e);
        DiscordEventsPool.getInstance().addEvent(event);
    }
}
