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

    private final Logger log = LoggerFactory.getLogger(KafkaBalanceServiceImpl.class);

    private final KafkaInMemoryBalanceDAO balanceDAO;

    private final KafkaTemplate<String, OperationRequest> kafkaTemplate;

    public KafkaBalanceServiceImpl(
            KafkaInMemoryBalanceDAO balanceDAO,
            KafkaTemplate<String, OperationRequest> kafkaTemplate
    ) {
        this.balanceDAO = balanceDAO;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public BigDecimal findBalanceById(final String key) {
        return balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);
    }

    public void makeDeposit(final OperationRequest request) {
        String key = request.getClientName();
        BigDecimal value = request.getValue();

        var currentBalance = balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);

        balanceDAO.save(key, currentBalance.add(value));
    }

    public void makeWithdrawal(final OperationRequest request) {
        String key = request.getClientName();
        BigDecimal value = request.getValue();

        var currentBalance = balanceDAO.getByKey(key)
                .orElse(BigDecimal.ZERO);

        balanceDAO.save(key, currentBalance.subtract(value));
    }

    public void sendToKafka(
            final String topic,
            final OperationRequest data
    ) {

        String key = data.getClientName();

        var sendResult = kafkaTemplate.send(topic, key, data);

        sendResult.addCallback(
                result -> {
                    assert result != null;
                    log.info("Success sent data: " + result.getProducerRecord().value()
                             + "\nTopic: " + topic);
                },
                error -> {
                    log.warn("Can't send to kafka: " + error.getMessage());
                }
        );

        kafkaTemplate.flush();
    }
}
