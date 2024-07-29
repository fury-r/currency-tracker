package com.bayztracker.service;

import com.bayztracker.data.AlertDetails;
import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.User;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.repositories.CurrencyRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.utils.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService( UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    @Transactional()
    public User getUser(Long id){
        Optional<User> user=  userRepository.findById(id);
        return user.orElse(null);

    }
}
