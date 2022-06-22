package com.nikskul.kafkaspringbanking;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "balance-change-event",
            groupId = "clients"
    )
    void listener(String data) {
        System.out.println("Listener received: " + data + " ");
    }
}
