package com.nikskul.kafkaspringbanking.produser;

import com.nikskul.kafkaspringbanking.request.CredentialsRequest;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.user.SimpleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperationRequestProducer implements KafkaSender<OperationRequest> {

    private final Logger log = LoggerFactory.getLogger(OperationRequestProducer.class);

    private final SimpleUserService userService;

    private final KafkaTemplate<String, OperationRequest> kafkaTemplate;

    public OperationRequestProducer(
            SimpleUserService simpleUserService,
            KafkaTemplate<String, OperationRequest> kafkaTemplate
    ) {
        this.userService = simpleUserService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToKafka(
            final String topic,
            final OperationRequest data
    ) {

        validateRequest(data);

        String key = data.getUsername();

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

        userService.authenticate(
                new CredentialsRequest(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        if (request.getValue().compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Operation value must be positive");

    }

}
