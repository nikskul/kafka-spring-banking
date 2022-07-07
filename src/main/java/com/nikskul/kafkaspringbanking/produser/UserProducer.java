package com.nikskul.kafkaspringbanking.produser;

import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.service.user.SimpleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class UserProducer implements KafkaSender<User> {

    private final Logger log = LoggerFactory.getLogger(OperationRequestProducer.class);

    private final SimpleUserService userService;

    private final KafkaTemplate<String, User> kafkaTemplate;

    public UserProducer(
            SimpleUserService userService,
            KafkaTemplate<String, User> kafkaTemplate
    ) {
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendToKafka(String topic, User user) {

        validateUser(user);

        String key = String.valueOf(user.getId());

        var sendResult = kafkaTemplate.send(topic, key, user);

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

    private void validateUser(User data) {
        // TODO: Implements method
    }
}
