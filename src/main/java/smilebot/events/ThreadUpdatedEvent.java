package smilebot.events;

import smilebot.service.DiscordService;

public class ThreadUpdatedEvent extends ThreadEvent {

    private boolean archived;

    public ThreadUpdatedEvent(long server_snowflake, long snowflake, long channel_snowflake, String name, boolean archived) {
        super(server_snowflake, snowflake, channel_snowflake, name);
        this.archived = archived;
    }

    @Override
    public void process() {
        DiscordService.getInstance().processThreadUpdate(snowflake, name, archived);
    }

}
