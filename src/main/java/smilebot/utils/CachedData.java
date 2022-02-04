package smilebot.utils;

import smilebot.model.IChannel;
import smilebot.model.IEmoji;
import smilebot.model.ISnowflake;
import smilebot.model.IUser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CachedData {

    private List<CachedServer> cachedServers;

    public CachedData() {
        cachedServers = new ArrayList<>();
    }

    public void addServer(long snowflake, String name, List<? extends IEmoji> emojis, List<? extends IUser> users, List<? extends IChannel> channels) {
        cachedServers.add(new CachedServer(snowflake, name, emojis, users, channels));
    }

    public void removeServer(long snowflake) {
        cachedServers.remove(snowflake);
    }

    public CachedServer getServerBySnowflake(long snowflake) {
        return findServer(new Predicate<CachedServer>() {
            @Override
            public boolean test(CachedServer cachedServer) {
                return cachedServer.getSnowflake() == snowflake;
            }
        });
    }

    public CachedServer getServerByName(String name) {
        return findServer(new Predicate<CachedServer>() {
            @Override
            public boolean test(CachedServer cachedServer) {
                return cachedServer.getName().equals(name);
            }
        });
    }

    private CachedServer findServer(Predicate<CachedServer> predicate) {
        for (CachedServer cs : cachedServers) {
            if (predicate.test(cs))
                return cs;
        }
        return null;
    }

}
