package smilebot.events;

import smilebot.service.DiscordService;

public class UserJoinEvent extends MemberEvent {

    private String name;

    public UserJoinEvent(long server_snowflake, long user_snowflake, String name) {
        super(server_snowflake, user_snowflake);
        this.name = name;
    }

    @Override
    public void process() {
        DiscordService.processUserJoin(server_snowflake, user_snowflake, name);
    }
}
