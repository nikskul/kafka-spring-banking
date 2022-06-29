package com.nikskul.kafkaspringbanking.loader;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProducerMessageLoader implements CommandLineRunner {

    private final String depositsTopic;
    private final String withdrawalsTopic;

    private final KafkaTemplate<String, OperationRequest> operationTemplate;

    public ProducerMessageLoader(
            @Value("${topics.deposit}") String depositsTopic,
            @Value("${topics.withdrawal}") String withdrawalsTopic,
            KafkaTemplate<String, OperationRequest> operationTemplate
    ) {
        this.depositsTopic = depositsTopic;
        this.withdrawalsTopic = withdrawalsTopic;
        this.operationTemplate = operationTemplate;
    }

    @Override
    public void run(String... args) {
        makeDeposits();
        makeWithdrawals();
    }

    private void makeDeposits() {
        for (int i = 0; i < 5; i++) {
            String key = "Ivan";
            BigDecimal data = BigDecimal.valueOf(i * 10);
            operationTemplate.send(
                    depositsTopic,
                    key,
                    new OperationRequest(key, data)
            );
        }
    }

    private void makeWithdrawals() {
        for (int i = 0; i < 2; i++) {
            String key = "Ivan";
            BigDecimal data = BigDecimal.valueOf(i * 10);
            operationTemplate.send(
                    withdrawalsTopic,
                    key,
                    new OperationRequest(key, data)
            );
        }
    }
}
