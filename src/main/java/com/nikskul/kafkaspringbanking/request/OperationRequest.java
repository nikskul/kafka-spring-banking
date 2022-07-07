package com.nikskul.kafkaspringbanking.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
public final class OperationRequest {
    @JsonProperty("username")
    private final String username;
    @JsonProperty("password")
    private final String password;
    @JsonProperty("value")
    private final BigDecimal value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public OperationRequest(
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password,
            @JsonProperty("value") final BigDecimal value
    ) {
        this.username = username;
        this.password = password;
        this.value = value;
    }
}
