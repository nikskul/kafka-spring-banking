package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.request.CredentialsRequest;
import com.nikskul.kafkaspringbanking.service.user.SimpleUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/public/users")
public class UserController {

    private final SimpleUserService userService;

    public UserController(SimpleUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void registerUser(@RequestBody final CredentialsRequest request) {
        var user = User.builder()
                .id(UUID.randomUUID())
                .username(request.username())
                .password(request.password())
                .build();

        userService.save(user);
    }
}
