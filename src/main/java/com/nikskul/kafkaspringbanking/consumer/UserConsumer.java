package com.nikskul.kafkaspringbanking.consumer;

import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.service.user.SimpleUserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;

public class UserConsumer {

    private final SimpleUserService userService;

    public UserConsumer(
            SimpleUserService userService
    ) {
        this.userService = userService;
    }


    @KafkaListener(
            id = "users-listener",
            containerFactory = "userListenerContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.client}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    )
            }
    )
    private void registerUser(final User user) {
        userService.save(user);
    }
}
