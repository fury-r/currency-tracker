package com.bayztracker.controller;

import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import com.bayztracker.payload.AlertPayload;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.repositories.UserRepository;
import com.bayztracker.scheduler.AlertScheduler;
import com.bayztracker.service.AlertService;
import com.bayztracker.service.CurrencyService;
import com.bayztracker.service.UserService;
import com.bayztracker.utils.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;  // For JSON conversion

    @MockBean
    private AlertService alertService;

    @MockBean
    private UserService userService;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    private AlertRepository alertRepository;

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private  AlertPayload alertPayload;

    @MockBean
    private AlertScheduler alertScheduler;

    private Long userId = 50000L;
    private Long roleId = 50000L;

    private User user;
    private Role role;
    private Alert alert;
    private Currency rupee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        role = new Role();
        role.setId(this.roleId);
        role.setName("USER");
        user.setUsername("test");
        user.setPassword("test");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setId(this.userId);

        rupee = new Currency();
        rupee.setId(10000);
        rupee.setCurrentPrice(500000.12F);
        rupee.setName("Rupee");
        rupee.setSymbol("₹");
        rupee.setEnabled(true);

        alert = new Alert();
        alert.setUser(user);
        alert.setCurrency(rupee);
        alert.setTargetPrice(600000.13F);
        alert.setId(1234);
        alert.setStatus(Status.NEW);

        when(alertService.getAllAlertsForUser(this.userId)).thenReturn(Collections.singletonList(alert));
        when(userService.getUser(userId)).thenReturn(user);
    }

    @Test
    public void checkGetAllAlertsForUser() throws Exception {

        mockMvc.perform(get("/alerts/user/" + this.userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].targetPrice").value(alert.getTargetPrice()))
                .andExpect(jsonPath("$[0].status").value(Status.NEW.toString()))
                .andExpect(jsonPath("$[0].user.id").value(this.userId));
    }


    @Test
    public void createAlert() throws Exception {
        Alert newAlert=new Alert();
        newAlert.setCurrency(rupee);
        newAlert.setTargetPrice(50000.55F);
        newAlert.setUser(alert.getUser());
        when(alertService.createAlert(any())).thenReturn(newAlert);


        String body=objectMapper.writeValueAsString(newAlert);
        newAlert.setStatus(Status.TRIGGERED);

        mockMvc.perform(post("/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.TRIGGERED.toString()))
                .andExpect(jsonPath("$.user.id").value(newAlert.getUser().getId()))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()));

    }

    @Test
    public void createAlertWithoutCurrency() throws Exception {
        Alert newAlert=new Alert();
        newAlert.setTargetPrice(50000.55F);

        newAlert.setUser(alert.getUser());
        when(alertService.createAlert(any())).thenReturn(newAlert);


        String body=objectMapper.writeValueAsString(newAlert);
        newAlert.setStatus(Status.TRIGGERED);

        mockMvc.perform(post("/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN+";charset=UTF-8"))
                .andExpect(content().string("currency is missing"));

    }


    @Test
    public void updateAlert() throws Exception {
        Alert newAlert = new Alert();
        newAlert.setId(1000L);
        newAlert.setTargetPrice(50000.55F);
        newAlert.setUser(alert.getUser());
        newAlert.setStatus(Status.ACKED);
        newAlert.setCurrency(rupee);

        when(alertService.updateAlert(any(), any())).thenReturn(newAlert);


        String body = objectMapper.writeValueAsString(newAlert);

        mockMvc.perform(put("/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.ACKED.toString()))
                .andExpect(jsonPath("$.user.id").value(newAlert.getUser().getId()))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()));

        mockMvc.perform(put("/alerts/" + newAlert.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.ACKED.toString()))
                .andExpect(jsonPath("$.user.id").value(newAlert.getUser().getId()))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()));
    }

    @Test
    public void updateAlertStatus() throws Exception {
        Alert newAlert=new Alert();
        newAlert.setId(1000L);
        newAlert.setTargetPrice(50000.55F);
        newAlert.setUser(alert.getUser());
        newAlert.setStatus(Status.CANCELLED);
        newAlert.setCurrency(rupee);
        when(alertRepository.findById(any())).thenReturn(Optional.of(newAlert));

        when(alertService.updateAlertStatus(any(), any())).thenReturn(newAlert);


        String body=objectMapper.writeValueAsString(newAlert);

        mockMvc.perform(put("/alerts/status/"+newAlert.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.CANCELLED.toString()))
                .andExpect(jsonPath("$.user.id").value(newAlert.getUser().getId()))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()));

    }


    @Test
    public void deleteAlert() throws Exception {
        Alert newAlert=new Alert();
        newAlert.setId(1000L);
        newAlert.setTargetPrice(50000.55F);
        newAlert.setUser(alert.getUser());
        newAlert.setStatus(Status.ACKED);
        newAlert.setCurrency(rupee);

        when(alertService.deleteAlert(any())).thenReturn(true);



        mockMvc.perform(delete("/alerts/"+newAlert.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN+";charset=UTF-8"))
                .andExpect(content().string("Alert deleted"));
    }


    @Test
    public void createAlertAndWaitForTrigger() throws Exception {
        Currency pound = new Currency();
        pound.setCurrentPrice(500000.12F);
        pound.setName("Bitcoin");
        pound.setSymbol("&");
        pound.setEnabled(true);

        Alert newAlert = new Alert();
        newAlert.setUser(user);
        newAlert.setCurrency(rupee);
        newAlert.setTargetPrice(600000.13F);
        newAlert.setStatus(Status.NEW);
        newAlert.setId(5000);

        when(alertService.createAlert(any())).thenReturn(newAlert);


        String body=objectMapper.writeValueAsString(newAlert);
        newAlert.setStatus(Status.TRIGGERED);

        mockMvc.perform(post("/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.TRIGGERED.toString()))
                .andExpect(jsonPath("$.user.id").value(newAlert.getUser().getId()))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()));
        pound.setCurrentPrice(6000002.15F);

        when(alertService.getAlertsOnStatus(Status.NEW)).thenReturn(Collections.singletonList(newAlert));



        alertScheduler.checkIfAlertTrigger();
        when(alertService.getAlert(newAlert.getId())).thenReturn(newAlert);

        mockMvc.perform(get("/alerts/" + newAlert.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newAlert.getId()))
                .andExpect(jsonPath("$.targetPrice").value(alert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.TRIGGERED.toString()))
                .andExpect(jsonPath("$.user.id").value(this.userId));

        newAlert.setStatus(Status.ACKED);


        when(alertService.updateAlertStatus(newAlert.getId(),Status.ACKED)).thenAnswer( invocation->{
            newAlert.setStatus(invocation.getArgument(1));
            return newAlert;
        });
         body=objectMapper.writeValueAsString(newAlert);

        mockMvc.perform(put("/alerts/status/"+newAlert.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()))
                .andExpect(jsonPath("$.status").value(Status.ACKED.toString()))
                .andExpect(jsonPath("$.user.id").value(newAlert.getUser().getId()))
                .andExpect(jsonPath("$.targetPrice").value(newAlert.getTargetPrice()));

    }

    @Test
    public  void updateTriggeredAlertToCancel() throws Exception {
        alert.setStatus(Status.CANCELLED);

        String body=objectMapper.writeValueAsString(alert);

        when(alertService.updateAlertStatus(eq(alert.getId()),eq(Status.valueOf(alert.getStatus())))).thenThrow( new IllegalStateException("Alert cannot be cancelled because it is not in the NEW state. Current status: " + Status.TRIGGERED));
        alert.setStatus(Status.TRIGGERED);

        mockMvc.perform(put("/alerts/status/"+alert.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN+";charset=UTF-8"))
                .andExpect(content().string(containsString("Alert cannot be cancelled because it is not in the NEW state. Current status: " + alert.getStatus())));

    }
}
