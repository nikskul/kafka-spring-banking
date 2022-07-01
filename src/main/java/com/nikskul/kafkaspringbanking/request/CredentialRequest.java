package com.nikskul.kafkaspringbanking.request;

public record CredentialRequest (String username, String password) {

    public CredentialRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

}
