package smilebot.model.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DiscordEntityClass {
    boolean containedIn() default false;
}
