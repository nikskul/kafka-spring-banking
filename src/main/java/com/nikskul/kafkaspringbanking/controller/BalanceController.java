package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.CredentialRequest;
import com.nikskul.kafkaspringbanking.service.BankClientService;
import com.nikskul.kafkaspringbanking.service.CalculateBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/balances")
public class BalanceController {

    private final CalculateBalanceService calculateBalanceService;

    public BalanceController(
            CalculateBalanceService calculateBalanceService
    ) {
        this.calculateBalanceService = calculateBalanceService;
    }

    @GetMapping
    public ResponseEntity<BigDecimal> getBalance(@RequestBody final CredentialRequest request) {

        BigDecimal balance
                = calculateBalanceService.getBalance(request.username());

        return ResponseEntity.ok(balance);
    }
}
