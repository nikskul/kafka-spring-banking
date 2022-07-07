package com.nikskul.kafkaspringbanking.dao;

import com.nikskul.kafkaspringbanking.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryBankClientDAO implements DataAccessObject<UUID, User> {

    private final Map<UUID, User> clients = new HashMap<>();

    @Override
    public void save(UUID key, User value) {
        clients.put(key, value);
    }

    @Override
    public Optional<User> getByKey(UUID key) {
        return Optional.ofNullable(clients.get(key));
    }

    public Optional<User> getByUsername(String username) {
        return clients.values()
                .stream()
                .filter(client -> username.equals(client.getUsername()))
                .findFirst();
    }

}
