package smilebot.events;

import smilebot.service.DiscordService;

public class UserLeftEvent extends MemberEvent {

    public UserLeftEvent(long server_snowflake, long user_snowflake) {
        super(server_snowflake, user_snowflake);
    }

    @Override
    public void process() {
        DiscordService.getInstance().processUserLeft(server_snowflake, user_snowflake);
    }

}
