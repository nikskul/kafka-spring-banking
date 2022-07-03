package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.OperationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/deposits")
public class DepositController {

    private final OperationService operationService;

    public DepositController(OperationService depositService) {
        this.operationService = depositService;
    }

    @PostMapping
    public void makeDeposit(@RequestBody final OperationRequest request) {
        operationService.makeDeposit(request);
    }
}
