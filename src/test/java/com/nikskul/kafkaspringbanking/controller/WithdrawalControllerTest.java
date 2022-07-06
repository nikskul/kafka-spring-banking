package com.nikskul.kafkaspringbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikskul.kafkaspringbanking.produser.OperationRequestProducer;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WithdrawalController.class)
class WithdrawalControllerTest {

    private final String API_ENDPOINT = "/api/v1/withdrawals";

    @MockBean
    OperationRequestProducer sender;

    @Autowired
    MockMvc mockMvc;

    @Test
    void makeWithdrawal() throws Exception {

        OperationRequest request = new OperationRequest(
                "testSuccess",
                BigDecimal.TEN
        );

        doNothing().when(sender).sendToKafka("${topics.withdrawal}", request);

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

}