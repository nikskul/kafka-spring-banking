package com.nikskul.kafkaspringbanking;

import com.nikskul.kafkaspringbanking.controller.BalanceController;
import com.nikskul.kafkaspringbanking.controller.ClientController;
import com.nikskul.kafkaspringbanking.controller.DepositController;
import com.nikskul.kafkaspringbanking.controller.WithdrawalController;
import com.nikskul.kafkaspringbanking.exeption.CustomErrorController;
import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.BankClientService;
import com.nikskul.kafkaspringbanking.service.CalculateBalanceService;
import com.nikskul.kafkaspringbanking.service.OperationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class KafkaSpringBankingApplicationTests {

    @Autowired
    KafkaTemplate<String, BankClient> clientKafkaTemplate;
    @Autowired
    KafkaTemplate<String, OperationRequest> operationRequestKafkaTemplate;

    @Autowired
    BalanceController balanceController;
    @Autowired
    ClientController clientController;
    @Autowired
    DepositController depositController;
    @Autowired
    WithdrawalController withdrawalController;

    @Autowired
    CustomErrorController errorController;

    @Autowired
    BankClientService bankClientService;
    @Autowired
    CalculateBalanceService calculateBalanceService;
    @Autowired
    OperationService operationService;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(clientKafkaTemplate);
        Assertions.assertNotNull(operationRequestKafkaTemplate);

        Assertions.assertNotNull(balanceController);
        Assertions.assertNotNull(clientController);
        Assertions.assertNotNull(depositController);
        Assertions.assertNotNull(withdrawalController);

        Assertions.assertNotNull(errorController);

        Assertions.assertNotNull(bankClientService);
        Assertions.assertNotNull(calculateBalanceService);
        Assertions.assertNotNull(operationService);
    }
}
