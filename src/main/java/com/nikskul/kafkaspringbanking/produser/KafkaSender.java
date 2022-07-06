package com.nikskul.kafkaspringbanking.produser;

public interface KafkaSender<T> {

    void sendToKafka(String topic, T data);

}
