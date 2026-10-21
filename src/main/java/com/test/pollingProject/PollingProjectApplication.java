package com.test.pollingProject;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableResilientMethods
public class PollingProjectApplication {
    public static void main(String[] args) {

        // Load the .env variables into the system properties
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        // Run the project
        SpringApplication.run(PollingProjectApplication.class, args);
        log.info("Polling app starting staticPollingInterval={} livePollingInterval={}",
                System.getProperty("STATIC_POLLING_INTERVAL"),
                System.getProperty("LIVE_POLLING_INTERVAL"));
    }
}
