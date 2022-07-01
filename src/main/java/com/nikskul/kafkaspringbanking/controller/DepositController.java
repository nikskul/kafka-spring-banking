package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/deposits")
public class DepositController {

    private final String topic;

    private final KafkaTemplate<String, OperationRequest> kafkaTemplate;

    public DepositController(
            @Value("${topics.deposit}") String topic,
            KafkaTemplate<String, OperationRequest> kafkaTemplate
    ) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void makeDeposit(@RequestBody final OperationRequest request) {
        String key = request.getUsername();
        kafkaTemplate.send(topic, key, request);
    }
}
