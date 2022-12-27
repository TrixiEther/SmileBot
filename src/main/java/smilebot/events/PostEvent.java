package smilebot.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PostEvent implements IDiscordEvent {

    private List<String> posts = new ArrayList<>();

    private Guild guild;
    private User user;
    private MessageChannel channel;

    public PostEvent(Guild guild, User user, MessageChannel channel) {
        this.guild = guild;
        this.user = user;
        this.channel = channel;
    }

    public void addPost(String str) {
        this.posts.add(str);
    }

    public List<String> getPosts() {
        return posts;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    @Override
    public void process() throws InvocationTargetException, IllegalAccessException, InstantiationException {

    }
}
