package smilebot.events;

import com.sun.tools.javac.util.Pair;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.model.CustomFields;
import smilebot.service.DiscordService;

import java.lang.reflect.InvocationTargetException;

public class GeneralEntityCreateEvent extends GeneralEntityEvent {

    public GeneralEntityCreateEvent(long server, Class<?> clazz, EventMetadata metadata, Pair<CustomFields, Object>... values) throws CustomIdAnnotationNotFoundException {
        super(server, clazz, metadata, values);
    }

    @Override
    public void process() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiscordService.getInstance().processGeneralCreateEvent(this);
    }

}
