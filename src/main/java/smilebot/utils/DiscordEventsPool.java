package smilebot.utils;

import smilebot.events.IDiscordEvent;

import java.util.LinkedList;
import java.util.Queue;

public class DiscordEventsPool extends Thread {

    private static final DiscordEventsPool POOL = new DiscordEventsPool();

    private final Queue<IDiscordEvent> events;
    private final Queue<IDiscordEvent> delayedEvents;

    public boolean isRunning = true;
    public boolean isLocked = false;

    public DiscordEventsPool() {
        this.events = new LinkedList<>();
        this.delayedEvents = new LinkedList<>();
    }

    public void addEvent(IDiscordEvent e) {
        if (e != null) {
            if (!isLocked || e.isBlockingBypass())
                this.events.add(e);
            else
                this.delayedEvents.add(e);
        }
    }

    public static DiscordEventsPool getInstance() {
        return POOL;
    }

    public void lock() {
        this.isLocked = true;
        if (!events.isEmpty()) {
            delayedEvents.addAll(events);
        }
    }

    public void unlock() {
        this.isLocked = false;
        events.addAll(delayedEvents);
    }

    @Override
    public void run() {
        try {
            while (isRunning) {

                while (!events.isEmpty()) {
                    events.poll().process();
                }

                Thread.sleep(1000);
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }

}
