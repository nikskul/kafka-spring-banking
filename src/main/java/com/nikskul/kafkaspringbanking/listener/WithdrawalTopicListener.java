package com.nikskul.kafkaspringbanking.listener;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.BalanceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalTopicListener {

    private final BalanceService balanceService;

    public WithdrawalTopicListener(
            BalanceService balanceService
    ) {
        this.balanceService = balanceService;
    }

    @KafkaListener(
            id = "calculatorWithdrawalsListener",
            containerFactory = "operationRequestListenerContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.withdrawal}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    ),
            }
    )
    private void calculateWithdrawals(final OperationRequest request) {
        balanceService.makeWithdrawal(request);
    }

}
