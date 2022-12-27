package smilebot.configuration;

public class BotCommand {


    private BotCommandItem commandItem;
    private String command;
    private String description;

    public BotCommand(BotCommandItem commandItem, String command, String description) {
        this.commandItem = commandItem;
        this.command = command;
        this.description = description;
    }

    public BotCommandItem getCommandItem() {
        return commandItem;
    }

    public void setCommandItem(BotCommandItem commandItem) {
        this.commandItem = commandItem;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
