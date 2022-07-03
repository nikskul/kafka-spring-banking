package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.exeption.BadRequestException;
import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateBalanceService {

    private final Map<String, BigDecimal> clientBalances = new HashMap<>();

    public BigDecimal getBalance(String username) {
        if (clientBalances.get(username) == null)
            throw new BadRequestException(String.format("Bank Account \" %s \" not exist.", username));
        return clientBalances.get(username);
    }

    @KafkaListener(
            id = "calculatorClientListener",
            concurrency = "1",
            containerGroup = "calculator",
            containerFactory = "clientContainerFactory",
            topicPartitions = @TopicPartition(
                    topic = "${topics.client}",
                    partitions = {"0-1000"},
                    partitionOffsets = @PartitionOffset(
                            partition = "*",
                            initialOffset = "0"
                    )
            )
    )
    public void registerNewAccountBalance(BankClient client) {
        if (!clientBalances.containsKey(client.getUsername()))
            clientBalances.put(client.getUsername(), BigDecimal.ZERO);
    }

    @KafkaListener(
            id = "calculatorDepositsListener",
            containerFactory = "clientOperationContainerFactory",
            concurrency = "2",
            containerGroup = "calculator",
            topicPartitions = @TopicPartition(
                    topic = "${topics.deposit}",
                    partitions = {"0-1000"},
                    partitionOffsets = @PartitionOffset(
                            partition = "*",
                            initialOffset = "0"
                    )
            )
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
            containerFactory = "clientOperationContainerFactory",
            concurrency = "2",
            containerGroup = "calculator",
            topicPartitions = @TopicPartition(
                    topic = "${topics.withdrawal}",
                    partitions = {"0-1000"},
                    partitionOffsets = @PartitionOffset(
                            partition = "*",
                            initialOffset = "0"
                    )
            )
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
}
