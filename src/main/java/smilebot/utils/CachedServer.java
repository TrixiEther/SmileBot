package smilebot.utils;

import smilebot.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CachedServer extends AbstractCachedObject implements IServer {

    private String content;
    private List<CachedEmoji> emojis;
    private List<CachedUser> users;
    private List<CachedChannel> channels;

    public CachedServer(long snowflake, String content, List<? extends IEmoji> emoji, List<? extends IUser> users, List<? extends IChannel> channels) {
        super(snowflake);
        this.content = content;
        this.emojis = new ArrayList<>();
        this.users = new ArrayList<>();
        this.channels = new ArrayList<>();
        for (IEmoji e : emoji) {
            this.emojis.add(new CachedEmoji(e.getSnowflake(), e.getEmoji()));
        }
        for (IUser u : users) {
            this.users.add(new CachedUser(u.getSnowflake(), u.getName()));
        }
        for (IChannel c : channels) {
            this.channels.add(new CachedChannel(c.getSnowflake(), c.getName()));
        }
    }

    public void setName(String name) {
        this.content = name;
    }

    public String getName() {
        return content;
    }

    public void setEmojis(List<CachedEmoji> emoji) {
        this.emojis = emoji;
    }

    public List<CachedEmoji> getEmojis() {
        return emojis;
    }

    public void addEmoji(CachedEmoji e) {
        this.emojis.add(e);
    }

    public void removeEmoji(long snowflake) {
        CachedEmoji e = findEmojiBySnowflake(snowflake);
        if (e != null)
            emojis.remove(e);
    }

    @Override
    public CachedEmoji findEmojiBySnowflake(long snowflake) {
        return findEmoji(new Predicate<CachedEmoji>() {
            @Override
            public boolean test(CachedEmoji cachedEmoji) {
                return cachedEmoji.getSnowflake() == snowflake;
            }
        });
    }

    public CachedEmoji findEmojiByContent(String content) {
        return findEmoji(new Predicate<CachedEmoji>() {
            @Override
            public boolean test(CachedEmoji cachedEmoji) {
                return cachedEmoji.getEmoji().equals(content);
            }
        });
    }

    @Override
    public CachedUser findUserBySnowflake(long snowflake) {
        return findUser(new Predicate<CachedUser>() {
            @Override
            public boolean test(CachedUser cachedUser) {
                return cachedUser.getSnowflake() == snowflake;
            }
        });
    }

    public CachedUser findUserByName(String name) {
        return findUser(new Predicate<CachedUser>() {
            @Override
            public boolean test(CachedUser cachedUser) {
                return cachedUser.getName().equals(name);
            }
        });
    }

    @Override
    public CachedChannel findChannelBySnowflake(long snowflake) {
        return findChannel(new Predicate<CachedChannel>() {
            @Override
            public boolean test(CachedChannel cachedChannel) {
                return cachedChannel.getSnowflake() == snowflake;
            }
        });
    }

    @Override
    public IDiscordThread findThreadBySnowflake(long snowflake) {
        return null;
    }

    @Override
    public IMessageContainer findMessageContainerBySnowflake(long snowflake) {
        return null;
    }

    public CachedChannel findChannelByName(String name) {
        return findChannel(new Predicate<CachedChannel>() {
            @Override
            public boolean test(CachedChannel cachedChannel) {
                return cachedChannel.getName().equals(name);
            }
        });
    }

    private CachedEmoji findEmoji(Predicate<CachedEmoji> predicate) {
        for (CachedEmoji e : emojis) {
            if (predicate.test(e))
                return e;
        }
        return null;
    }

    private CachedUser findUser(Predicate<CachedUser> predicate) {
        for (CachedUser u : users) {
            if (predicate.test(u))
                return u;
        }
        return null;
    }

    private CachedChannel findChannel(Predicate<CachedChannel> predicate) {
        for (CachedChannel c : channels) {
            if (predicate.test(c))
                return c;
        }
        return null;
    }

}
