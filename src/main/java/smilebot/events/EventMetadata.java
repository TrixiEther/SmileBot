package smilebot.events;

import com.sun.tools.javac.util.Pair;

enum EventMetadataType {
    REFRESH_SERVER
}

public class EventMetadata {

    private boolean isServerRefreshRequired = false;

    public EventMetadata(){}

    public EventMetadata(Pair<EventMetadataType, Object>... data) {
        for (Pair<EventMetadataType, Object> p : data) {
            switch (p.fst) {
                case REFRESH_SERVER: {
                    this.isServerRefreshRequired = (boolean) p.snd;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    public boolean isServerRefreshRequired() {
        return isServerRefreshRequired;
    }

}
