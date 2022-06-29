package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(
        topics = {
                "${topics.deposit}",
                "${topics.withdrawal}"
        },
        groupId = "logger",
        containerFactory = "clientRequestContainerFactory"
)
public class KafkaListeners {

    private final Logger log = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaHandler
    void balanceChangeEventListener(OperationRequest request) {
        log.info("Listener received: " + request);
    }

}
