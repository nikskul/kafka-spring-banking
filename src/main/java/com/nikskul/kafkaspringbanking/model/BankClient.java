package com.nikskul.kafkaspringbanking.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BankClient {

    @JsonProperty("username")
    private final String username;
    @JsonProperty("password")
    private final String password;

    @JsonProperty("firstName")
    private final String firstName;
    @JsonProperty("middleName")
    private final String middleName;
    @JsonProperty("lastName")
    private final String lastName;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BankClient(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("middleName") String middleName,
            @JsonProperty("lastName") String lastName
    ) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
