package com.bayztracker.controller;

import com.bayztracker.model.Alert;
import com.bayztracker.model.Currency;
import com.bayztracker.model.Role;
import com.bayztracker.model.User;
import com.bayztracker.payload.AlertPayload;
import com.bayztracker.repositories.AlertRepository;
import com.bayztracker.service.AlertService;
import com.bayztracker.service.CurrencyService;
import com.bayztracker.utils.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
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
    private CurrencyService currencyService;
    @MockBean
    private AlertRepository alertRepository;

    @MockBean
    private  AlertPayload alertPayload;

    private Long userId = 50000L;
    private Long roleId = 50000L;

    private User user;
    private Role role;
    private Alert alert;
    private Currency bitcoin;

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

        bitcoin = new Currency();
        bitcoin.setCurrentPrice(500000.12F);
        bitcoin.setName("Bitcoin");
        bitcoin.setSymbol("&");
        bitcoin.setEnabled(true);

        alert = new Alert();
        alert.setUser(user);
        alert.setCurrency(bitcoin);
        alert.setTargetPrice(600000.13F);
        alert.setStatus(Status.NEW);

        when(alertService.getAllAlertsForUser(this.userId)).thenReturn(Collections.singletonList(alert));
    }

    @Test
    public void checkGetAllAlertsForUser() throws Exception {
        mockMvc.perform(get("/alerts/" + this.userId))
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
        newAlert.setCurrency(bitcoin);
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
        newAlert.setCurrency(bitcoin);

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
        newAlert.setCurrency(bitcoin);
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

        mockMvc.perform(put("/alerts/status/"+newAlert.getId()+"/"+Status.CANCELLED.toString())
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
        newAlert.setCurrency(bitcoin);

        when(alertService.deleteAlert(any())).thenReturn(true);



        mockMvc.perform(delete("/alerts/"+newAlert.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN+";charset=UTF-8"))
                .andExpect(content().string("Alert deleted"));
    }

}
