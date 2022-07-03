package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import com.nikskul.kafkaspringbanking.service.OperationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/withdrawals")
public class WithdrawalController {

    private final OperationService operationService;

    public WithdrawalController(
            final OperationService operationService
    ) {
        this.operationService = operationService;
    }

    @PostMapping
    public void makeWithdrawal(@RequestBody final OperationRequest request) {
        operationService.makeWithdrawal(request);
    }
}
