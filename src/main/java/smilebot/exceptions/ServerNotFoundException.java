package smilebot.exceptions;

public class ServerNotFoundException extends Exception {

    public ServerNotFoundException() {
        super("The server was not found in the database, it may not have been initialized yet");
    }

}
