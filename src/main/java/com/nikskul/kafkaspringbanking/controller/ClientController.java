package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.model.BankClient;
import com.nikskul.kafkaspringbanking.service.BankClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final BankClientService bankClientService;

    public ClientController(BankClientService bankClientService) {
        this.bankClientService = bankClientService;
    }

    @PostMapping
    public void registerClient(@RequestBody final BankClient client) {
        bankClientService.register(client);
    }
}
