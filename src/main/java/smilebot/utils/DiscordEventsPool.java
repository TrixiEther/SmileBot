package smilebot.utils;

import smilebot.events.IDiscordEvent;

import java.util.LinkedList;
import java.util.Queue;

public class DiscordEventsPool extends Thread {

    private static final DiscordEventsPool POOL = new DiscordEventsPool();

    private final Queue<IDiscordEvent> events;

    public boolean isRunning = true;

    public DiscordEventsPool() {
        this.events = new LinkedList<>();
    }

    public void addEvent(IDiscordEvent e) {
        this.events.add(e);
    }

    public static DiscordEventsPool getInstance() {
        return POOL;
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
