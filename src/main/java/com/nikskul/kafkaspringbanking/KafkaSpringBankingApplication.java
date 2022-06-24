package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.request.DepositRequest;
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
    CommandLineRunner commandLineRunner(KafkaTemplate<String, BankClient> kafkaTemplate) {

        return args -> {
            for (int i = 0; i < 5; i++) {
                String key = UUID.randomUUID().toString();
                kafkaTemplate.send(
                        "clients",
                        key,
                        new BankClient(
                                UUID.fromString(key),
                                "Ivan",
                                "Ivanovich",
                                "Ivanov"
                        )
                );
            }
        };
    }
}
