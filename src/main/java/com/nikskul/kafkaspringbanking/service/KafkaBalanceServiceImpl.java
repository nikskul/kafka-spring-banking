package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.dao.KafkaInMemoryBalanceDAO;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KafkaBalanceServiceImpl implements BalanceService {

    private final KafkaInMemoryBalanceDAO balanceDAO;


    public KafkaBalanceServiceImpl(
            KafkaInMemoryBalanceDAO balanceDAO
    ) {
        this.balanceDAO = balanceDAO;
    }

    @Override
    public BigDecimal findBalanceById(final String key) {
        return balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);
    }
}
