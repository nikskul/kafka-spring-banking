package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.model.BankClient;
import org.springframework.kafka.core.KafkaTemplate;

public class ClientController {

    private final KafkaTemplate<String, BankClient> clientTemplate;

    public ClientController(KafkaTemplate<String, BankClient> clientTemplate) {
        this.clientTemplate = clientTemplate;
    }

}
