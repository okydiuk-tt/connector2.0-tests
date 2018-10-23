package com.timetrade.connector2.qa.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
public class Config {

    private static final Log logger = LogFactory.getLog(Config.class);
    public static Properties properties;

    static {
        // get environment
        String prop = System.getProperty("env"); // use to run on Jenkins Machine
        if (prop == null) {
            prop = System.getenv("env"); // use to run local PC
        }
        if (prop == null) {
            prop = "dev";
        }

        // read properties
        properties = new Properties();
        try {
            File file = new ClassPathResource(prop + ".properties").getFile();
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            logger.error("Unable to read Environment properties", e);
        }

    }
}
