package smilebot.monitored;

public interface IInternalEventProducer {

    void subscribeToInternalEvents(IInternalEventListener listener);

}
