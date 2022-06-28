package com.nikskul.kafkaspringbanking.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public record DepositRequest(
        @JsonProperty("name") String name,
        @JsonProperty("deposit") Integer deposit
) {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DepositRequest(
            @JsonProperty("name") final String name,
            @JsonProperty("deposit") final Integer deposit
    ) {
        this.name = name;
        this.deposit = deposit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DepositRequest) obj;
        return Objects.equals(this.name, that.name) &&
               Objects.equals(this.deposit, that.deposit);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (deposit != null ? deposit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DepositRequest[" +
               "name=" + name + ", " +
               "deposit=" + deposit + ']';
    }


}
