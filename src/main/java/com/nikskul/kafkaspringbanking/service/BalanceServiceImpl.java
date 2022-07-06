package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.dao.BalanceDAOInMemoryImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceDAOInMemoryImpl balanceDAO;

    public BalanceServiceImpl(
            BalanceDAOInMemoryImpl balanceDAO
    ) {
        this.balanceDAO = balanceDAO;
    }

    @Override
    public BigDecimal findBalanceById(final String key) {
        return balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);
    }
}
