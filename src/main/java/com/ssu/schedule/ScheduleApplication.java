package com.ssu.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ScheduleApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ScheduleApplication.class, args);
        SpringApplication.exit(context);
    }
}