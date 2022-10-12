package smilebot.events;

import com.sun.tools.javac.util.Pair;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.model.CustomFields;
import smilebot.service.DiscordService;

import java.lang.reflect.InvocationTargetException;

public class GeneralEntityUpdateEvent extends GeneralEntityEvent{

    public GeneralEntityUpdateEvent(long server, Class<?> clazz, EventMetadata metadata, Pair<CustomFields, Object>... values) throws CustomIdAnnotationNotFoundException {
        super(server, clazz, metadata, values);
    }

    public void invokeSetterMethods(Object entity) throws InvocationTargetException, IllegalAccessException {
        for (FieldData fieldData : fieldData) {
            fieldData.setter.invoke(entity, fieldData.value);
        }
    }

    @Override
    public void process() throws InvocationTargetException, IllegalAccessException {
        DiscordService.getInstance().processGeneralUpdateEvent(this);
    }

}
