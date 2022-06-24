package com.nikskul.kafkaspringbanking.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nikskul.kafkaspringbanking.model.BankClient;
import lombok.Data;

@Data
public class DepositRequest {

//    @JsonProperty("receiver")
//    private final BankClient receiver;

    @JsonProperty("deposit")
    private final Integer deposit;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DepositRequest(
//            @JsonProperty("receiver") final BankClient receiver,
            @JsonProperty("deposit") final Integer deposit
    ) {
//        if (client == null) {
//            throw new IllegalArgumentException("Client was null.");
//        }
//
//        if (deposit < 0)
//            throw new IllegalArgumentException("Deposit value must be more than 0.");

//        this.receiver = receiver;
        this.deposit = deposit;
    }
}
