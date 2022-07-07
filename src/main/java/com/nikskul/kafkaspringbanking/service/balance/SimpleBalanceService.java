package com.nikskul.kafkaspringbanking.service.balance;

import com.nikskul.kafkaspringbanking.dao.InMemoryBalanceDAO;
import com.nikskul.kafkaspringbanking.service.BalanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SimpleBalanceService implements BalanceService<String> {

    private final InMemoryBalanceDAO balanceDAO;

    public SimpleBalanceService(
            InMemoryBalanceDAO balanceDAO
    ) {
        this.balanceDAO = balanceDAO;
    }

    @Override
    public BigDecimal getBalance(final String key) {
        return balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);
    }
}
