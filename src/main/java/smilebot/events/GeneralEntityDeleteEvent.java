package smilebot.events;

import com.sun.tools.javac.util.Pair;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.model.CustomFields;
import smilebot.service.DiscordService;

public class GeneralEntityDeleteEvent extends GeneralEntityEvent{
    public GeneralEntityDeleteEvent(long server, Class<?> clazz, EventMetadata metadata, Pair<CustomFields, Object>... values) throws CustomIdAnnotationNotFoundException {
        super(server, clazz, metadata, values);
    }

    @Override
    public void process() {
        DiscordService.getInstance().processGeneralDeleteEvent(this);
    }
}
