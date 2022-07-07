package com.nikskul.kafkaspringbanking.request;

public record CredentialsRequest(
        String username,
        String password
) {

}
