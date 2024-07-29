package com.bayztracker;

import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import liquibase.pro.packaged.A;
import liquibase.pro.packaged.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.bayztracker")
public class BayzTrackerApplication {

    public static void main(String[] args) {

        SpringApplication.run(BayzTrackerApplication.class, args);
    }

}
