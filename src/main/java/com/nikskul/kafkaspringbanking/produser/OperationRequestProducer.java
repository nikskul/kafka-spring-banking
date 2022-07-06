package com.nikskul.kafkaspringbanking.produser;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperationRequestProducer implements KafkaSender<OperationRequest> {

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

        validateRequest(data);

        String key = data.getClientName();

        var sendResult = kafkaTemplate.send(topic, key, data);

        sendResult.addCallback(
                result -> {
                    assert result != null;
                    log.info("Topic: " + topic + ". " +
                             "Success sent data: " + result.getProducerRecord().value());
                },
                error -> log.warn("Can't send to kafka: " + error.getMessage())
        );

        kafkaTemplate.flush();
    }

    private void validateRequest(final OperationRequest request) {

        if (request.getValue().compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Operation value must be positive");

    }

}
