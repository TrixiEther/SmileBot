package smilebot.model.annotations;

import smilebot.model.CustomFields;

import java.lang.annotation.*;

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiscordEntityConstructor {
    CustomFields[] arguments();
}
