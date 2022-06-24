package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import com.nikskul.kafkaspringbanking.service.CalculateBalanceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final CalculateBalanceService calculateBalanceService;

    public KafkaListeners(CalculateBalanceService calculateBalanceService) {
        this.calculateBalanceService = calculateBalanceService;
    }

    @KafkaListener(
            topics = "${topics.balance}",
            groupId = "clients",
            containerFactory = "factory"
    )
    void depositListener(DepositRequest request) {
        System.out.println("Listener received:\n " + request);
        calculateBalanceService.calculate();
    }
}
