package com.nikskul.kafkaspringbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikskul.kafkaspringbanking.exeption.BadRequestException;
import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.service.CalculateBalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BalanceController.class)
class BalanceControllerTest {

    private final String API_ENDPOINT = "/api/v1/balances";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculateBalanceService service;

    @Test
    void testGetBalanceZero() throws Exception {
        Mockito.when(service.getBalance("testZero")).thenReturn(BigDecimal.ZERO);

        BankClient client = new BankClient(
                "testZero",
                "test",
                "Hello",
                "Kafka",
                "Test"
        );

        ObjectMapper mapper = new ObjectMapper();

        String jsonClient = mapper.writeValueAsString(client);

        MvcResult result
                = mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT)
                        .content(jsonClient)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        var actual = result.getResponse().getContentAsString();

        assertEquals("0", actual);

    }

    @Test
    void testGetBalanceThrow() throws Exception {
        Mockito.when(service.getBalance("testThrow"))
                .thenThrow(new BadRequestException("Bank Account \"testThrow\" not exist."));

        BankClient client = new BankClient(
                "testThrow",
                "test",
                "Hello",
                "Kafka",
                "Test"
        );

        ObjectMapper mapper = new ObjectMapper();

        String jsonClient = mapper.writeValueAsString(client);

        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT)
                .content(jsonClient)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}