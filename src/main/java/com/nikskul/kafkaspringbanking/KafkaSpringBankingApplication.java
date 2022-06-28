package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@SpringBootApplication
public class KafkaSpringBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner autoRunnableExample(
            KafkaTemplate<String, DepositRequest> kafkaTemplate,
            @Value("${topics.balance}") String topic
    ) {

        return args -> {
            for (int i = 0; i < 5; i++) {
                String key = "Ivan";
                Integer data = i * 10;
                kafkaTemplate.send(
                        topic,
                        key,
                        new DepositRequest(key, data)
                );
            }
        };
    }
}
