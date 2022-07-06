package com.nikskul.kafkaspringbanking.produser;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OperationRequestProducer implements KafkaProducer<OperationRequest> {

    private final Logger log = LoggerFactory.getLogger(OperationRequestProducer.class);

    private final KafkaTemplate<String, OperationRequest> kafkaTemplate;

    public OperationRequestProducer(
            KafkaTemplate<String, OperationRequest> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToKafka(
            final String topic,
            final OperationRequest data
    ) {

        String key = data.getClientName();

        var sendResult = kafkaTemplate.send(topic, key, data);

        sendResult.addCallback(
                result -> {
                    assert result != null;
                    log.info("Success sent data: " + result.getProducerRecord().value()
                             + "\nTopic: " + topic);
                },
                error -> log.warn("Can't send to kafka: " + error.getMessage())
        );

        kafkaTemplate.flush();
    }

}
