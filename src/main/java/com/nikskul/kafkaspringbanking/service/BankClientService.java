package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.model.BankClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankClientService {

    private Map<String, BankClient> clientMap = new HashMap<>();

    public BankClient create(final BankClient toSave) {

        // TODO: Validate incoming model

        if (clientMap.containsKey(toSave.getUsername()))
            throw new RuntimeException("Username already exist.");

        BankClient client = new BankClient(
                toSave.getUsername(),
                toSave.getPassword(),
                toSave.getFirstName(),
                toSave.getMiddleName(),
                toSave.getLastName()
        );

        clientMap.put(client.getUsername(), client);

        return client;
    }

    public List<BankClient> findAll() {
        return new ArrayList<>(clientMap.values());
    }

    public Optional<BankClient> findByUsername(String username) {
        return Optional.ofNullable(clientMap.get(username));
    }


}
