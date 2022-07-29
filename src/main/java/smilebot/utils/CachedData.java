package smilebot.utils;

import smilebot.model.IChannel;
import smilebot.model.IEmoji;
import smilebot.model.ISnowflake;
import smilebot.model.IUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class CachedData {

    private Set<Long> uninitializedServers;
    private Set<Long> requiredRefresh;
    private List<CachedServer> cachedServers;

    public CachedData() {
        cachedServers = new ArrayList<>();
        uninitializedServers = new HashSet<>();
        requiredRefresh = new HashSet<>();
    }

    public void addServer(long snowflake, String name, List<? extends IEmoji> emojis, List<? extends IUser> users, List<? extends IChannel> channels) {
        cachedServers.removeIf(s -> s.getSnowflake() == snowflake);
        requiredRefresh.remove(snowflake);
        cachedServers.add(new CachedServer(snowflake, name, emojis, users, channels));
        uninitializedServers.remove(snowflake);
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

    public void setUninitializedServer(long snowflake) {
        uninitializedServers.add(snowflake);
    }

    public void setRequiredRefreshServer(long snowflake) {
        requiredRefresh.add(snowflake);
    }

    public boolean isUninitialized(long snowflake) {
        for (long sn : uninitializedServers) {
            if (sn == snowflake) {
                return true;
            }
        }
        return false;
    }

    public boolean isRequiredRefresh(long snowflake) {
        for (long sn : requiredRefresh) {
            if (sn == snowflake) {
                return true;
            }
        }
        return false;
    }

    private CachedServer findServer(Predicate<CachedServer> predicate) {
        for (CachedServer cs : cachedServers) {
            if (predicate.test(cs))
                return cs;
        }
        return null;
    }

}
