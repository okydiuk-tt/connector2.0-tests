package com.timetrade.ews.notifications;

import com.timetrade.ews.notifications.configuration.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author vadym.moroz
 */
@SpringBootApplication
@Import({Config.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
