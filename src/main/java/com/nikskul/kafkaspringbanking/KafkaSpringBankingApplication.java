package com.nikskul.kafkaspringbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class KafkaSpringBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringBankingApplication.class, args);
    }

}