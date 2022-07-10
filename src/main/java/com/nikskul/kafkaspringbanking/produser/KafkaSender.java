package com.nikskul.kafkaspringbanking.produser;

/**
 * Uses for send messages to kafka
 *
 * @param <T> Model Type
 */
public interface KafkaSender<T> {

    void sendToKafka(String topic, T data);

}
