package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.exeption.BadRequestException;
import com.nikskul.kafkaspringbanking.model.BankClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;

@Service
public class BankClientService {

    private final String topic;

    private final KafkaTemplate<String, BankClient> clientTemplate;

    private final Map<String, BankClient> clientMap = new HashMap<>();

    public BankClientService(
            @Value("${topics.client}") final String topic,
            final KafkaTemplate<String, BankClient> clientTemplate
    ) {
        this.topic = topic;
        this.clientTemplate = clientTemplate;
    }

    public void register(final BankClient client) {
        validateBeforeRegister(client);

        String key = client.getUsername();
        ListenableFuture<SendResult<String, BankClient>> future = clientTemplate.send(topic, key, client);
        clientTemplate.flush();
    }

    private void validateBeforeRegister(BankClient client) {
        if (clientMap.containsKey(client.getUsername()))
            throw new BadRequestException("Username already exist.");
    }

    public List<BankClient> findAll() {
        return new ArrayList<>(clientMap.values());
    }

    public Optional<BankClient> findByUsername(String username) {
        return Optional.ofNullable(clientMap.get(username));
    }

    @KafkaListener(
            id = "bankClientServiceListener",
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
    private void save(final BankClient toSave) {
        LoggerFactory.getLogger(BankClientService.class).info("Saving: " + toSave);
        clientMap.put(toSave.getUsername(), toSave);
    }

}

