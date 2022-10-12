package smilebot.events;

import com.sun.tools.javac.util.Pair;
import smilebot.exceptions.CustomIdAnnotationNotFoundException;
import smilebot.model.*;
import smilebot.model.annotations.DiscordEntityClass;
import smilebot.model.annotations.DiscordEntityConstructor;
import smilebot.model.annotations.DiscordEntityField;
import smilebot.model.annotations.DiscordEntityMethod;

import javax.persistence.Id;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class FieldData {
    public boolean isId = false;
    public CustomFields fieldName;
    public Object value;
    public Method setter = null;
}

public abstract class GeneralEntityEvent implements IDiscordEvent {

    protected final Class<?> clazz;
    protected List<FieldData> fieldData = new ArrayList<>();

    protected EventMetadata metadata;

    protected boolean isContainedIn = false;

    protected Object preservedId;
    protected Class<?> parentContainer;
    protected Method parentContainerSetter;
    protected Method childContainerAdder;

    protected GeneralEntityEvent(Object preservedId, Class<?> clazz, EventMetadata metadata, Pair<CustomFields, Object>... values) throws CustomIdAnnotationNotFoundException {
        this.clazz = clazz;
        this.preservedId = preservedId;
        this.metadata = metadata;

        if (clazz.isAnnotationPresent(DiscordEntityClass.class)) {
            DiscordEntityClass annotation = clazz.getAnnotation(DiscordEntityClass.class);
            if (annotation.containedIn())
                isContainedIn = true;
        }

        for (Pair<CustomFields, Object> value : values) {
            FieldData data = new FieldData();
            data.fieldName = value.fst;
            data.value = value.snd;
            fieldData.add(data);
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DiscordEntityField.class)) {
                DiscordEntityField annotation = field.getAnnotation(DiscordEntityField.class);
                for (FieldData data : fieldData) {
                    if (annotation.fieldName() == data.fieldName) {
                        data.isId = annotation.isId();
                    }
                }
                if (isContainedIn) {
                    if (annotation.isParentContainer()) {
                        parentContainer = field.getType();
                        Method[] parentMethods = parentContainer.getMethods();
                        for (Method method : parentMethods) {
                            if (method.isAnnotationPresent(DiscordEntityMethod.class)) {
                                DiscordEntityMethod parentMethodAnnotation = method.getAnnotation(DiscordEntityMethod.class);
                                if (parentMethodAnnotation.childContainerAdder() == clazz) {
                                    childContainerAdder = method;
                                }
                            }
                        }
                    }
                }
            }
        }

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(DiscordEntityMethod.class)) {
                DiscordEntityMethod annotation = method.getAnnotation(DiscordEntityMethod.class);
                for (FieldData data : fieldData) {
                    if (data.fieldName == annotation.setterFor()) {
                        data.setter = method;
                    }
                }
                if (isContainedIn) {
                    if (annotation.parentContainerSetter()) {
                        parentContainerSetter = method;
                    }
                }
            }
        }

    }

    public Class<?> getEntityClass() {
        return clazz;
    }

    private Field[] getClassAndSuperclassFields(Class<?> clazz, Field[] fields) {
        Field[] fields1 = fields;
        Field[] fields2 = clazz.getDeclaredFields();
        Field[] fields3 = new Field[fields1.length + fields2.length];
        int i = 0;
        for (int j = 0; j < fields1.length; j++, i++) {
            fields3[i] = fields1[j];
        }
        for (int k = 0; k < fields2.length; k++, i++) {
            fields3[i] = fields2[k];
        }
        Class<?> supClazz = clazz.getSuperclass();
        if (supClazz != null) {
            return getClassAndSuperclassFields(supClazz, fields3);
        }
        return fields3;
    }

    public String getIdColumnName() {
        Field[] fields = getClassAndSuperclassFields(clazz, new Field[]{});
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        return "";
    }

    public Object getIdValue() {
        for (FieldData field : fieldData) {
            if (field.fieldName == CustomFields.ID) {
                return field.value;
            }
        }
        return null;
    }

    public Object constructEntity() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(DiscordEntityConstructor.class)) {
                DiscordEntityConstructor annotation = constructor.getAnnotation(DiscordEntityConstructor.class);
                Object[] args = new Object[annotation.arguments().length];
                for (int i=0; i< annotation.arguments().length; i++) {
                    CustomFields customField = annotation.arguments()[i];
                    for (FieldData field : fieldData) {
                        if (customField == field.fieldName) {
                            args[i] = field.value;
                        }
                    }
                }
                return constructor.newInstance(args);
            }
        }
        return null;
    }

    public void invokeParentSetter(Object childEntity, Object parentEntity) throws InvocationTargetException, IllegalAccessException {
        if (parentContainerSetter != null)
            parentContainerSetter.invoke(childEntity, parentEntity);
    }

    public void invokeChildAdder(Object parentEntity, Object childEntity) throws InvocationTargetException, IllegalAccessException {
        if (childContainerAdder != null)
            childContainerAdder.invoke(parentEntity, childEntity);
    }

    public boolean isRefreshRequired() {
        return metadata.isServerRefreshRequired();
    }

    public Class<?> getPreserverClass() {
        if (parentContainer != null)
            return parentContainer;
        else return clazz;
    }

    public Object getPreservedId() {
        return preservedId;
    }
}
