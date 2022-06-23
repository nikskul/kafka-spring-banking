package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "balance-change-event",
            groupId = "clients",
            containerFactory = "factory"
    )
    void listener(DepositRequest request) {
        System.out.println("Listener received: " + request.getName() + " make deposit by " + request.getValue());
    }
}
