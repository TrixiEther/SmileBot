package smilebot.monitored;


import net.dv8tion.jda.api.hooks.ListenerAdapter;
import smilebot.events.IDiscordEvent;
import smilebot.events.PartialInitializationEvent;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class InternalListenerAdapter implements IInternalEventListener {

    public void onPartialInitialization(@Nonnull PartialInitializationEvent event) {}

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final ConcurrentMap<Class<?>, MethodHandle> methods = new ConcurrentHashMap<>();

    @Override
    public final void onEvent(@Nonnull IDiscordEvent event) {
        MethodHandle mh = methods.computeIfAbsent(event.getClass(), InternalListenerAdapter::findMethod);
        try {
            if (mh != null) {
                mh.invoke(this, event);
            }
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException)
                throw (RuntimeException) throwable;
            if (throwable instanceof Error)
                throw (Error) throwable;
            throw new IllegalStateException(throwable);
        }
    }

    private static MethodHandle findMethod(Class<?> clazz)
    {
        String name = clazz.getSimpleName();
        MethodType type = MethodType.methodType(Void.TYPE, clazz);
        try
        {
            name = "on" + name.substring(0, name.length() - "Event".length());
            return lookup.findVirtual(InternalListenerAdapter.class, name, type);
        }
        catch (NoSuchMethodException | IllegalAccessException ignored) {}
        return null;
    }
}
