package smilebot.model.annotations;

import smilebot.model.CustomFields;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiscordEntityMethod {
    CustomFields setterFor() default CustomFields.NONE;
    boolean parentContainerSetter() default false;
    Class<?> childContainerAdder() default Object.class;
}
