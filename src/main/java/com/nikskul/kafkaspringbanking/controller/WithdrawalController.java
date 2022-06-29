package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/withdrawals")
public class WithdrawalController {

    private final String topic;

    private final KafkaTemplate<String, OperationRequest> kafkaTemplate;

    public WithdrawalController(
            @Value("${topics.withdrawal}") String topic,
            KafkaTemplate<String, OperationRequest> kafkaTemplate
    ) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void makeWithdrawal(@RequestBody final OperationRequest request) {
        String key = request.getClientName();
        kafkaTemplate.send(topic, key, request);
    }
}
