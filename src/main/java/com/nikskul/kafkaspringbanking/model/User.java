package com.nikskul.kafkaspringbanking.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class User {

    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(
            @JsonProperty("id") UUID id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
