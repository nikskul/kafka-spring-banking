package com.nikskul.kafkaspringbanking.model;

import lombok.Data;

import java.util.UUID;

@Data
public class BankClient {

    private final UUID id;

    private final String username;
    private final String password;

    private final String firstName;
    private final String middleName;
    private final String lastName;

}
