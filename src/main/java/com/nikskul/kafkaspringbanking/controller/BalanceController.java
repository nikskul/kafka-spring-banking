package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.request.CredentialRequest;
import com.nikskul.kafkaspringbanking.service.CalculateBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/balances")
public class BalanceController {

    private final CalculateBalanceService calculateBalanceService;

    public BalanceController(CalculateBalanceService calculateBalanceService) {
        this.calculateBalanceService = calculateBalanceService;
    }

    @GetMapping
    public ResponseEntity<BigDecimal> getBalance(@RequestBody final CredentialRequest request) {

        Optional<BigDecimal> balanceOptional
                = calculateBalanceService.getBalance(request.username(), request.password());

        return balanceOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
