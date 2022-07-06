package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.service.BalanceServiceImpl;
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
    BalanceServiceImpl service;

    @Test
    void testGetBalanceZero() throws Exception {
        Mockito.when(service.findBalanceById("testZero")).thenReturn(BigDecimal.ZERO);

        MvcResult result
                = mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/testZero")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        var actual = result.getResponse().getContentAsString();

        assertEquals("0", actual);

    }

}