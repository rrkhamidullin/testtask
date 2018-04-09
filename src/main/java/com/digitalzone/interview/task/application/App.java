package com.digitalzone.interview.task.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableAutoConfiguration
@EnableJpaRepositories(App.SCAN_PACKAGE)
@EntityScan(App.SCAN_PACKAGE)
@ComponentScan(App.SCAN_PACKAGE)
public class App {
    static final String SCAN_PACKAGE = "com.digitalzone.interview.task";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
