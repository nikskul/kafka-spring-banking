package com.nikskul.kafkaspringbanking.service.user;

import com.nikskul.kafkaspringbanking.dao.InMemoryBankAccountDAO;
import com.nikskul.kafkaspringbanking.dao.InMemoryBankClientDAO;
import com.nikskul.kafkaspringbanking.model.BankAccount;
import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.request.CredentialsRequest;
import com.nikskul.kafkaspringbanking.service.UserService;
import com.nikskul.kafkaspringbanking.service.balance.UserBankAccountBalanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class SimpleUserService implements UserService<User> {

    private final InMemoryBankClientDAO bankClientDAO;
    private final InMemoryBankAccountDAO bankAccountDAO;

    public SimpleUserService(
            InMemoryBankClientDAO bankClientDAO,
            InMemoryBankAccountDAO bankAccountDAO) {
        this.bankClientDAO = bankClientDAO;
        this.bankAccountDAO = bankAccountDAO;
    }

    @Override
    public void save(User user) {
        var account = new BankAccount(
                UUID.randomUUID(),
                BigDecimal.ZERO,
                user
        );
        bankAccountDAO.save(account.getId(), account);
        bankClientDAO.save(user.getId(), user);
    }

    @Override
    public User getById(String id) {
        return bankClientDAO.getByKey(UUID.fromString(id))
                .orElseThrow(
                        () -> new RuntimeException("User with id: " + id + " not found.")
                );
    }

    @Override
    public User getByUsername(String username) {
        return bankClientDAO.getByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User with username: " + username + " not found")
                );
    }

    @Override
    public void authenticate(CredentialsRequest credentials) {

        var fromDB = getByUsername(credentials.username());

        if (!credentials.password().equals(fromDB.getPassword()))
            throw new RuntimeException("Username\\Password not correct.");
    }
}
