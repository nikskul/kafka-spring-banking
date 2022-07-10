package com.nikskul.kafkaspringbanking.consumer;

import com.nikskul.kafkaspringbanking.dao.InMemoryBankAccountDAO;
import com.nikskul.kafkaspringbanking.model.BankAccount;
import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.user.SimpleUserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Listen kafka deposit and withdrawal topics.
 * Call service for change balance
 */
@Component
public class OperationRequestConsumer {

    private final SimpleUserService userService;
    private final InMemoryBankAccountDAO bankAccountDAO;

    public OperationRequestConsumer(
            SimpleUserService userService,
            InMemoryBankAccountDAO bankAccountDAO
    ) {
        this.userService = userService;
        this.bankAccountDAO = bankAccountDAO;
    }

    @KafkaListener(
            id = "deposit-listener",
            containerFactory = "operationRequestListenerContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.deposit}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    )
            }
    )
    private void makeDeposit(final OperationRequest request) {

        validateRequest(request);

        String username = request.getUsername();
        BigDecimal deposit = request.getValue();

        var bankAccount = getBankAccount(username);

        var currentBalance = bankAccount.getBalance();

        var newBalance = currentBalance.add(deposit);

        bankAccount.setBalance(newBalance);

        bankAccountDAO.save(bankAccount.getId(), bankAccount);
    }

    @KafkaListener(
            id = "withdrawal-listener",
            containerFactory = "operationRequestListenerContainerFactory",
            topicPartitions = {
                    @TopicPartition(
                            topic = "${topics.withdrawal}",
                            partitions = "0-1000",
                            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")
                    ),
            }
    )
    private void makeWithdrawal(final OperationRequest request) {

        validateRequest(request);

        String username = request.getUsername();
        BigDecimal withdrawal = request.getValue();

        var bankAccount = getBankAccount(username);

        var currentBalance = bankAccount.getBalance();

        var newBalance = currentBalance.subtract(withdrawal);

        bankAccount.setBalance(newBalance);

        bankAccountDAO.save(bankAccount.getId(), bankAccount);
    }

    private BankAccount getBankAccount(String username) {
        var accountOwner = userService.getByUsername(username);

        return bankAccountDAO.getByOwnerId(accountOwner.getId())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Can't make deposit for not existing account by owner: "
                                + accountOwner.getUsername()
                        )
                );
    }

    private void validateRequest(final OperationRequest request) {

        if (request.getValue().compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Operation value must be positive");

    }

}
