package smilebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import smilebot.configuration.BotConfigurationProperties;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan("smilebot")
@ConfigurationPropertiesScan("smilebot.configuration")
public class SmileBotApplication {

    public static void main(String[] args) throws LoginException {
        SpringApplication.run(SmileBotApplication.class, args);
        SmileBotManager.getInstance().startSmileBot();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }

}
