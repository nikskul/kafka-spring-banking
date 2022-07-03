package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component

public class KafkaListeners {

    private final Logger log = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(
            topics = {
                    "${topics.deposit}",
                    "${topics.withdrawal}"
            },
            groupId = "logger",

            containerFactory = "clientOperationContainerFactory"
    )
    void balanceChangeEventListener(OperationRequest request) {
        log.info("Listener received: " + request);
    }

    @KafkaListener(
            topics = {
                    "${topics.client}"
            },
            groupId = "logger",
            containerFactory = "clientContainerFactory"
    )
    void clientsListener(BankClient request) {
        log.info("Listener received: " + request);
    }

}
