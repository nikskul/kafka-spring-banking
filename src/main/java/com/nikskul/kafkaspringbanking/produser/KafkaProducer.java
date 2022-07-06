package com.nikskul.kafkaspringbanking.produser;

import org.springframework.kafka.core.KafkaTemplate;

public interface KafkaProducer<T> {

    void sendToKafka(String topic, T data);

}
