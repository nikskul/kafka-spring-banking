package com.nikskul.kafkaspringbanking.consumer;

import com.nikskul.kafkaspringbanking.dao.BalanceDAOInMemoryImpl;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OperationRequestConsumer {

    private final BalanceDAOInMemoryImpl balanceDAO;

    public OperationRequestConsumer(
            BalanceDAOInMemoryImpl balanceDAO
    ) {
        this.balanceDAO = balanceDAO;
    }

    @KafkaListener(
            id = "deposit-listener",
            containerFactory = "operationRequestListenerContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.deposit}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    )
            }
    )
    private void makeDeposit(final OperationRequest request) {

        validateRequest(request);

        String key = request.getClientName();
        BigDecimal value = request.getValue();

        var currentBalance = balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);

        balanceDAO.save(key, currentBalance.add(value));
    }

    @KafkaListener(
            id = "withdrawal-listener",
            containerFactory = "operationRequestListenerContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.withdrawal}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    ),
            }
    )
    private void makeWithdrawal(final OperationRequest request) {

        validateRequest(request);

        String key = request.getClientName();
        BigDecimal value = request.getValue();

        var currentBalance = balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);

        balanceDAO.save(key, currentBalance.subtract(value));
    }

    private void validateRequest(final OperationRequest request) {

        if (request.getValue().compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Operation value must be positive");

    }

}
