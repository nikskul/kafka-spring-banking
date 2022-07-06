package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.produser.KafkaProducer;
import com.nikskul.kafkaspringbanking.produser.OperationRequestProducer;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.KafkaBalanceServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/deposits")
public class DepositController {

    private final KafkaProducer<OperationRequest> producer;

    private final String depositTopic;

    public DepositController(
            @Value("${topics.deposit}") String depositTopic,
            OperationRequestProducer producer
    ) {
        this.producer = producer;
        this.depositTopic = depositTopic;
    }

    @PostMapping
    public void makeDeposit(@RequestBody final OperationRequest request) {
        producer.sendToKafka(depositTopic, request);
    }
}
