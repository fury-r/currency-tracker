package com.bayztracker.data;

import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.repositories.CurrencyRepository;
import com.bayztracker.repositories.RoleRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class DataInitializerService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void insertDummyData() {

//        Set<Role> roles=new HashSet<Role>() ;
//        Role role1=new Role();
//        role1.setName("read");
//        Role r1=roleRepository.save(role1);
//        System.out.println(r1.getId()+" id----------------------------");
//        roles.add(r1);
//
//        User user1=new User();
//
//        user1.setUsername("fury-r");
//        user1.setRoles(roles);
//        user1.setPassword("test123");
//        user1.setId(50);
//        User user2=new User();
//
//        user2.setUsername("test");
//        user2.setRoles(roles);
//        user2.setPassword("test123");
//
//        User user_1=userRepository.save(user1);



        Optional<User> user=userRepository.findById(Long.valueOf(2));
        if(user.isPresent()){
            Currency bitcoin=new Currency();
            bitcoin.setCurrentPrice(500000.12F);
            bitcoin.setName("Bitcoin");
            bitcoin.setSymbol("&");
            bitcoin.setEnabled(true);
            currencyRepository.save(bitcoin);


            Alert alert=new Alert();
            alert.setUser(user.get());
            alert.setCurrency(bitcoin);
            alert.setTargetPrice(500000.125F);
            alert.setStatus(Status.NEW);

            alertRepository.save(alert);
            System.out.println("Dummy data inserted successfully.");
        }

    }
}
