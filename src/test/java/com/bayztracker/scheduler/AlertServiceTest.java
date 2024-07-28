package com.bayztracker.scheduler;

import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.repositories.CurrencyRepository;
import com.bayztracker.repositories.RoleRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.service.AlertService;
import com.bayztracker.service.CurrencyService;
import com.bayztracker.utils.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyService currencyService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertScheduler alertScheduler;

    private  static Logger LOG= LoggerFactory.getLogger(AlertScheduler.class);

    private  Long userId= 50000L;
    private Long roleId= 50000L;

    private  User user;
    private  Role role;
    private  Alert alert;
    private  Currency bitcoin;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        user=new User();
        role=new Role();
        role.setId(this.roleId);
        role.setName("USER");
        user.setUsername("test");
        user.setPassword("test");
        Set<Role> roles=new HashSet<>();
        user.setRoles(roles);
        user.setId(this.userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        bitcoin=new Currency();
        bitcoin.setCurrentPrice(500000.12F);
        bitcoin.setName("Bitcoin");
        bitcoin.setSymbol("&");
        bitcoin.setEnabled(true);

        Optional<User> savedUser=userRepository.findById(this.userId);

        alert=new Alert();
        alert.setUser(savedUser.get());
        alert.setCurrency(bitcoin);
        alert.setTargetPrice(600000.13F);
        alert.setStatus(Status.NEW);
        when(alertRepository.findById(alert.getId())).thenReturn(Optional.of(alert));
        when(alertService.getNewAlerts()).thenReturn(Collections.singletonList(alert));

    }


   @Test
   public void  checkAlertNotTriggerred()  {
           alertScheduler.checkIfAlertTrigger();
           verify(alertService,never()).updateAlertStatus(anyLong(),any(Status.class));
   }

    @Test
    public void  checkAlertIsTriggered(){
        alert.getCurrency().setCurrentPrice(700000.13F);
        alertScheduler.checkIfAlertTrigger();
        verify(alertService,times(1)).updateAlertStatus(alert.getId(),Status.TRIGGERED);
    }


}
