package com.nikskul.kafkaspringbanking.dao;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class KafkaInMemoryBalanceDAO implements BalanceDAO<String, BigDecimal> {

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
