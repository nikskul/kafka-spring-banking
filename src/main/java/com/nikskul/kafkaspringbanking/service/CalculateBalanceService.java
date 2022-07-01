package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@KafkaListener
public class CalculateBalanceService {

    private final Map<String, BigDecimal> clientBalances = new HashMap<>();

    @KafkaListener(
            id = "calculatorDepositsListener",
            containerFactory = "clientRequestContainerFactory"
    )
    private void calculateDeposits(final OperationRequest request) {
        if (clientBalances.containsKey(request.getUsername())) {
            BigDecimal accumulator = clientBalances.get(request.getUsername());
            accumulator = accumulator.add(request.getValue());
            clientBalances.put(request.getUsername(), accumulator);
        } else {
            clientBalances.put(request.getUsername(), request.getValue());
        }
    }

    @KafkaListener(
            id = "calculatorWithdrawalsListener",
            containerFactory = "clientRequestContainerFactory"
    )
    private void calculateWithdrawals(final OperationRequest request) {
        if (clientBalances.containsKey(request.getUsername())) {
            BigDecimal accumulator = clientBalances.get(request.getUsername());
            accumulator = accumulator.subtract(request.getValue());
            clientBalances.put(request.getUsername(), accumulator);
        } else {
            clientBalances.put(request.getUsername(), request.getValue());
        }
    }

    public Optional<BigDecimal> getBalance(String username, String password) {

        // TODO: Аутентификация клиента


        return Optional.ofNullable(clientBalances.get(username));
    }
}
