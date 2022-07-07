package com.nikskul.kafkaspringbanking.service.balance;

import com.nikskul.kafkaspringbanking.dao.InMemoryBankAccountDAO;
import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.service.BalanceService;
import com.nikskul.kafkaspringbanking.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserBankAccountBalanceService implements BalanceService<User> {

    private final InMemoryBankAccountDAO bankAccountDAO;

    public UserBankAccountBalanceService(
            InMemoryBankAccountDAO bankAccountDAO
    ) {
        this.bankAccountDAO = bankAccountDAO;
    }

    @Override
    public BigDecimal getBalance(final User client) {

        var ownerId = client.getId();

        var account = bankAccountDAO.getByOwnerId(ownerId)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        return account.getBalance();
    }
}
