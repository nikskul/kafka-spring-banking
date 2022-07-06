package com.nikskul.kafkaspringbanking.listener;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.KafkaBalanceServiceImpl;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class DepositTopicListener {

    private final KafkaBalanceServiceImpl balanceService;

    public DepositTopicListener(KafkaBalanceServiceImpl balanceService) {
        this.balanceService = balanceService;
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
    private void calculateDeposits(final OperationRequest request) {
        balanceService.makeDeposit(request);
    }

}
