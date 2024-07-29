package com.bayztracker.scheduler;


import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import com.bayztracker.repositories.AlertRepository;

import com.bayztracker.repositories.CurrencyRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.service.AlertService;
import com.bayztracker.utils.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertScheduler alertScheduler;


    private  Long userId= 5L;
    private Long roleId= 5L;

    private  User user;
    private  Role role;
    private  Alert alert;
    private  Currency currency;


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
    }


   @Test
   public void checkAlertNotTriggered()  {
           alertScheduler.checkIfAlertTrigger();
           verify(alertService,never()).updateAlertStatus(anyLong(),any(Status.class));
   }

    @Test
    public void  checkAlertIsTriggered(){
        alert.getCurrency().setCurrentPrice(700000.13F);
        when(alertService.updateAlertStatus(alert.getId(),Status.ACKED)).thenAnswer(invocationOnMock -> {
            alert.setStatus(invocationOnMock.getArgument(1));
            return  alert;
        });

        alertScheduler.checkIfAlertTrigger();
        verify(alertService).updateAlertStatus(alert.getId(),Status.TRIGGERED);

        Alert update=alertService.updateAlertStatus(alert.getId(),Status.ACKED);


        assertThat(update.getStatus()).isEqualTo(Status.ACKED.toString());

    }

}
