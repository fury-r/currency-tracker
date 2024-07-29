package com.bayztracker;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.bayztracker")
public class BayzTrackerApplication {

    public static void main(String[] args) {

        SpringApplication.run(BayzTrackerApplication.class, args);
    }

}
