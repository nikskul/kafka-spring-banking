package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.request.DepositRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class KafkaSpringBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, DepositRequest> kafkaTemplate) {
        return args -> {
            for (int i = 0; i < 5; i++) {
                kafkaTemplate.send("balance-change-event", new DepositRequest("Ivan", 10*(i+1)));
            }
        };
    }
}
