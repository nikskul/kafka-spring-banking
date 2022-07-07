package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.request.CredentialsRequest;

import java.util.Optional;

public interface UserService<T extends User> {

    void save(T user);

    T getById(String id);
    T getByUsername(String username);

    void authenticate(CredentialsRequest credentials);

}
