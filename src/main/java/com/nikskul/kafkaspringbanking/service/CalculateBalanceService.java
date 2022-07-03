package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculateBalanceService {

    private final Map<String, BigDecimal> clientBalances = new HashMap<>();

    @KafkaListener(
            id = "calculatorDepositsListener",
            containerFactory = "clientRequestContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.deposit}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    )
            }
    )
    private void calculateDeposits(final OperationRequest request) {
        if (clientBalances.containsKey(request.getClientName())) {
            BigDecimal accumulator = clientBalances.get(request.getClientName());
            accumulator = accumulator.add(request.getValue());
            clientBalances.put(request.getClientName(), accumulator);
        } else {
            clientBalances.put(request.getClientName(), request.getValue());
        }
    }

    @KafkaListener(
            id = "calculatorWithdrawalsListener",
            containerFactory = "clientRequestContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.withdrawal}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    ),
            }
    )
    private void calculateWithdrawals(final OperationRequest request) {
        if (clientBalances.containsKey(request.getClientName())) {
            BigDecimal accumulator = clientBalances.get(request.getClientName());
            accumulator = accumulator.subtract(request.getValue());
            clientBalances.put(request.getClientName(), accumulator);
        } else {
            clientBalances.put(request.getClientName(), request.getValue());
        }
    }

    public Optional<BigDecimal> getBalance(String clientName) {
        return Optional.ofNullable(clientBalances.get(clientName));
    }
}
