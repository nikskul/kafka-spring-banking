package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.service.CalculateBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/balances")
public class BalanceController {

    private final CalculateBalanceService calculateBalanceService;

    public BalanceController(CalculateBalanceService calculateBalanceService) {
        this.calculateBalanceService = calculateBalanceService;
    }

    @GetMapping("{name}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("name") String clientName) {

        Optional<BigDecimal> balanceOptional = calculateBalanceService.getBalance(clientName);

        return balanceOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
