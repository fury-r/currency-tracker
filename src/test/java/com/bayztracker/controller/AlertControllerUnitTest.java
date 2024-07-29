package com.bayztracker.controller;

import com.bayztracker.data.AlertDetails;
import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.repositories.CurrencyRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.scheduler.AlertScheduler;
import com.bayztracker.service.AlertService;
import com.bayztracker.utils.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class AlertControllerUnitTest {
    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private AlertService alertService;

    @InjectMocks
    private AlertScheduler alertScheduler;


    private  Long userId= 5L;
    private Long roleId= 5L;

    private User user;
    private Role role;
    private  Alert alert;
    private Currency currency;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        this.user=new User();
        this.role=new Role();
        this.role.setId(this.roleId);
        this.role.setName("USER");
        this.user.setUsername("test");
        this.user.setPassword("test");
        Set<Role> roles=new HashSet<>();
        this.user.setRoles(roles);
        this.user.setId(this.userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        this.currency =new Currency();
        this.currency.setId(10);
        this.currency.setCurrentPrice(500000.12F);
        this.currency.setName("Bitcoin");
        this.currency.setSymbol("&");
        this.currency.setEnabled(true);


        this.alert=new Alert();
        this.alert.setUser(this.user);
        this.alert.setCurrency(currency);
        this.alert.setTargetPrice(600000.13F);
        this.alert.setStatus(Status.NEW);
        alert.setId(11);
        when(alertRepository.findById(alert.getId())).thenReturn(Optional.of(alert));
        when(alertService.getAlertsOnStatus(Status.NEW)).thenReturn(Collections.singletonList(alert));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(this.user));
        when(currencyRepository.findById(any())).thenReturn(Optional.ofNullable(this.currency));

    }

    @Test
    public  void createAlert(){

        alertService.createAlert(new AlertDetails(userId, currency.getId(),10F, Status.TRIGGERED));
        verify(alertRepository,times(1)).save(any(Alert.class));

    }

    @Test
    public  void readAlert(){
        Alert read=alertService.getAlert(alert.getId());
        verify(alertRepository,times(1)).findById(alert.getId());
        assertThat(read).isEqualTo(this.alert);

    }

    @Test
    public  void updateAlert(){
        alertService.updateAlert(alert.getId(),new AlertDetails(userId, currency.getId(),11F,Status.TRIGGERED));
        verify(alertRepository,times(1)).save(any(Alert.class));

    }

    @Test
    public  void deleteAlert(){
        alertService.deleteAlert(alert.getId());
        verify(alertRepository,times(1)).deleteById(alert.getId());

    }


}
