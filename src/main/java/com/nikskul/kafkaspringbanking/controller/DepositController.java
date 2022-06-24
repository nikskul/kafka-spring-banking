package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/deposits")
public class DepositController {

    private final String TOPIC;

    private final KafkaTemplate<String, DepositRequest> kafkaTemplate;

    public DepositController(
            @Value("${topics.balance}") String topic,
            KafkaTemplate<String, DepositRequest> kafkaTemplate
    ) {
        TOPIC = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void makeDeposit(@RequestBody final DepositRequest request) {
//        String key = request.getReceiver().getClientId().toString();
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), request);
    }
}
