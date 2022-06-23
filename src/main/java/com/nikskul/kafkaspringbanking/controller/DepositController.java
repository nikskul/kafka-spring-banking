package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/deposits")
public class DepositController {

    KafkaTemplate<String, DepositRequest> kafkaTemplate;

    @PostMapping
    public void makeDeposit(@RequestBody DepositRequest request) {
        kafkaTemplate.send("balance-change-event", request);
    }
}
