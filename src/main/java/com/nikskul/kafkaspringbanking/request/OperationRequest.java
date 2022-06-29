package com.nikskul.kafkaspringbanking.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public final class OperationRequest {
    @JsonProperty("clientName")
    private final String clientName;
    @JsonProperty("value")
    private final BigDecimal value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public OperationRequest(
            @JsonProperty("clientName") final String clientName,
            @JsonProperty("value") final BigDecimal value
    ) {
        this.clientName = clientName;
        this.value = value;
    }
}
