package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.produser.KafkaProducer;
import com.nikskul.kafkaspringbanking.produser.OperationRequestProducer;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/withdrawals")
public class WithdrawalController {

    private final String withdrawalTopic;

    private final KafkaProducer<OperationRequest> producer;

    public WithdrawalController(
            @Value("${topics.withdrawal}") String withdrawalTopic,
            OperationRequestProducer producer
    ) {
        this.withdrawalTopic = withdrawalTopic;
        this.producer = producer;
    }

    @PostMapping
    public void makeWithdrawal(@RequestBody final OperationRequest request) {
        producer.sendToKafka(withdrawalTopic, request);
    }
}
