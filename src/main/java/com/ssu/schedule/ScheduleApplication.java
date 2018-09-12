package com.ssu.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class ScheduleApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ScheduleApplication.class, args);
        SpringApplication.exit(context);
    }
}