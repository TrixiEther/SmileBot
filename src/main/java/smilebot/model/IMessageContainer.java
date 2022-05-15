package smilebot.model;

public interface IMessageContainer {

    boolean isContainMessage(Message message);
    void addMessage(Message message);

}
