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
//    @Autowired
//    private  DataInitializerService dataInitializerService;
    public static void main(String[] args) {
//        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Eclipselink_JPA" );
//        EntityManager entitymanager = emfactory.createEntityManager( );
//        entitymanager.getTransaction().begin();
//
//        Set<Role> roles=new HashSet<Role>() ;
//        Role role1=new Role();
//        role1.setName("read");
//        Role role2=new Role();
//        role2.setName("read and write");
//        roles.add(role2);
//        User user1=new User();
//
//        user1.setUsername("fury-r");
//        user1.setRoles(roles);
//        user1.setPassword("test123");
//        User user2=new User();
//
//        user2.setUsername("test");
//        user2.setRoles(roles);
//        user2.setPassword("test123");
//
//        entitymanager.persist(role1);
//        entitymanager.persist(role2);
//
//        entitymanager.persist(user1);
//        entitymanager.persist(user2);
//
//        Currency bitcoin=new Currency();
//        bitcoin.setCurrentPrice(500000.12F);
//        bitcoin.setName("Bitcoin");
//        bitcoin.setEnabled(true);
//
//        entitymanager.persist(bitcoin);
//
//        Alert alert=new Alert();
//        alert.setUser(user1);
//        alert.setCurrency(bitcoin);
//        alert.setTargetPrice(500000.125F);
//        entitymanager.persist(alert);
//        entitymanager.getTransaction().commit();
//        entitymanager.close();
//        emfactory.close();

        SpringApplication.run(BayzTrackerApplication.class, args);
    }


    @PostConstruct
    public  void  init(){
//        dataInitializerService.insertDummyData();
    }
}
