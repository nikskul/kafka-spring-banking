package com.nikskul.kafkaspringbanking.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class BankAccount {

    @JsonProperty("id")
    private final UUID id;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("owner")
    private final User owner;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BankAccount(
            @JsonProperty("id") final UUID id,
            @JsonProperty("balance") final BigDecimal balance,
            @JsonProperty("owner") final User owner
    ) {
        this.id = id;
        this.balance = balance;
        this.owner = owner;
    }
}
