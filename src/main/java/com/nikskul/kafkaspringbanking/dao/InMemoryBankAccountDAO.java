package com.nikskul.kafkaspringbanking.dao;

import com.nikskul.kafkaspringbanking.model.BankAccount;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class InMemoryBankAccountDAO implements DataAccessObject<UUID, BankAccount> {

    Map<UUID, BankAccount> accounts = new HashMap<>();

    @Override
    public void save(UUID key, BankAccount value) {
        accounts.put(key, value);
    }

    @Override
    public Optional<BankAccount> getByKey(UUID key) {
        return Optional.ofNullable(accounts.get(key));
    }

    public Optional<BankAccount> getByOwnerId(UUID ownerId) {
        return accounts.values()
                .stream()
                .filter(account -> ownerId.equals(account.getOwner().getId()))
                .findFirst();
    }
}
