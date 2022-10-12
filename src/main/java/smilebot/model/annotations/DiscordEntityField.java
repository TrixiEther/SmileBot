package smilebot.model.annotations;

import smilebot.model.CustomFields;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiscordEntityField {
    CustomFields fieldName() default CustomFields.NONE;
    boolean isId() default false;
    boolean isParentContainer() default false;
}
