package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.KafkaBalanceServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/withdrawals")
public class WithdrawalController {

    private final String withdrawalTopic;

    private final KafkaBalanceServiceImpl balanceService;

    public WithdrawalController(
            @Value("${topics.withdrawal}") String withdrawalTopic,
            KafkaBalanceServiceImpl balanceService
    ) {
        this.withdrawalTopic = withdrawalTopic;
        this.balanceService = balanceService;
    }

    @PostMapping
    public void makeWithdrawal(@RequestBody final OperationRequest request) {
        balanceService.sendToKafka(withdrawalTopic, request);
    }
}
