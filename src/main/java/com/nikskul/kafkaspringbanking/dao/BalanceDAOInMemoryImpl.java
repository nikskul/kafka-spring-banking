package com.nikskul.kafkaspringbanking.dao;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class BalanceDAOInMemoryImpl implements BalanceDAO<String, BigDecimal> {

    Map<String, BigDecimal> balances = new HashMap<>();

    @Override
    public void save(String key, BigDecimal value) {
        balances.put(key, value);
    }

    @Override
    public Optional<BigDecimal> getByKey(String key) {
        return Optional.ofNullable(balances.get(key));
    }

}
