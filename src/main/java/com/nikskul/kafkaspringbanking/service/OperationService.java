package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.exeption.BadRequestException;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.math.BigDecimal;

@Service
public class OperationService {

    private final BankClientService service;

    private final String depositTopic;
    private final String withdrawalTopic;

    private final KafkaTemplate<String, OperationRequest> kafkaTemplate;

    public OperationService(
            BankClientService service,
            @Value("${topics.deposit}") final String depositTopic,
            @Value("${topics.withdrawal}") final String withdrawalTopic,
            final KafkaTemplate<String, OperationRequest> kafkaTemplate
    ) {
        this.service = service;
        this.depositTopic = depositTopic;
        this.withdrawalTopic = withdrawalTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void makeDeposit(final OperationRequest request) {
        validateRequest(request);

        String key = request.getUsername();
        ListenableFuture<SendResult<String, OperationRequest>> future = kafkaTemplate.send(depositTopic, key, request);
        kafkaTemplate.flush();
    }

    public void makeWithdrawal(final OperationRequest request) {
        validateRequest(request);

        String key = request.getUsername();
        ListenableFuture<SendResult<String, OperationRequest>> future = kafkaTemplate.send(withdrawalTopic, key, request);
        kafkaTemplate.flush();
    }

    private void validateRequest(final OperationRequest request) {
        if(service.findByUsername(request.getUsername()).isEmpty())
            throw new BadRequestException("Username\\Password not much");

        if (request.getValue().compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("Value must be more than zero");
    }
}
