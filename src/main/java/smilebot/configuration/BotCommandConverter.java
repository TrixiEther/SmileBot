package smilebot.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class BotCommandConverter implements Converter<String, BotCommand> {

    @Override
    public BotCommand convert(String s) {

        String[] data = s.split(":");
        BotCommandItem item = null;

        for (BotCommandItem i : BotCommandItem.values()) {
            if (i.toString().equals(data[0])) {
                item = i;
                break;
            }
        }

        return new BotCommand(item, data[1], data[2]);
    }

}
