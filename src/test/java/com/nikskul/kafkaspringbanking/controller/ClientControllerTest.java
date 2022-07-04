package com.nikskul.kafkaspringbanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikskul.kafkaspringbanking.exeption.BadRequestException;
import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.service.BankClientService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    private final String API_ENDPOINT = "/api/v1/clients";

    @MockBean
    BankClientService service;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testRegisterClientSuccess() throws Exception {

        BankClient client = new BankClient(
                "testSuccess",
                "test",
                "Hello",
                "Kafka",
                "Test"
        );

        ObjectMapper mapper = new ObjectMapper();

        String jsonClient = mapper.writeValueAsString(client);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT)
                .content(jsonClient)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testRegisterClientThrow() throws Exception {

        BankClient client = new BankClient(
                "testThrow",
                "test",
                "I Am",
                "Already",
                "Exist"
        );

        Mockito.doThrow(new BadRequestException("Already Exist!")).when(service).register(client);

        ObjectMapper mapper = new ObjectMapper();

        String jsonClient = mapper.writeValueAsString(client);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT)
                .content(jsonClient)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}