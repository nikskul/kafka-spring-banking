package com.nikskul.kafkaspringbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikskul.kafkaspringbanking.exeption.BadRequestException;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.OperationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WithdrawalController.class)
class WithdrawalControllerTest {

    private final String API_ENDPOINT = "/api/v1/withdrawals";

    @MockBean
    OperationService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testWithdrawalSuccess() throws Exception {

        OperationRequest request = new OperationRequest(
                "testSuccess",
                "test",
                BigDecimal.TEN
        );

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testWithdrawalThrow() throws Exception {

        OperationRequest request = new OperationRequest(
                "testThrow",
                "test",
                BigDecimal.TEN.negate()
        );

        Mockito.doThrow(new BadRequestException("Value Must Be Positive!"))
                .when(service).makeWithdrawal(request);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}