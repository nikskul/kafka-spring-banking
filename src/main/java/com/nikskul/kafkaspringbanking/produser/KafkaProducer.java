package com.nikskul.kafkaspringbanking.produser;

public interface KafkaProducer<T> {

    void sendToKafka(String topic, T data);

}
