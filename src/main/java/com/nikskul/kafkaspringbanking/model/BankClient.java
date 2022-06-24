package com.nikskul.kafkaspringbanking.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BankClient {

    private final UUID clientId;

    private String firstName;

    private String lastName;

    private String secondName;

    private BigDecimal balance = BigDecimal.ZERO;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BankClient(
            @JsonProperty("clientId") final UUID clientId,
            @JsonProperty("firstName")final String firstName,
            @JsonProperty("lastName") final String lastName,
            @JsonProperty("secondName") final String secondName
    ) {
        if (
                firstName.trim().length() == 0
                || secondName.trim().length() == 0
                || lastName.trim().length() == 0
        ) {
            throw new IllegalArgumentException("Full name can't have null field.");
        }

        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
    }

    public void setFirstName(final String firstName) {
        if (firstName.trim().length() == 0) {
            throw new IllegalArgumentException("First name length can't be 0.");
        }
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        if (lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Last name length can't be 0.");
        }
        this.lastName = lastName;
    }

    public void setSecondName(final String secondName) {
        if (secondName.trim().length() == 0) {
            throw new IllegalArgumentException("Second name length can't be 0.");
        }
        this.secondName = secondName;
    }
}




