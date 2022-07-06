package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.service.KafkaBalanceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/balances")
public class BalanceController {

    private final KafkaBalanceServiceImpl balanceService;

    public BalanceController(KafkaBalanceServiceImpl balanceServiceImpl) {
        this.balanceService = balanceServiceImpl;
    }

    @GetMapping("{name}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("name") String clientName) {

        BigDecimal balance = balanceService.findBalanceById(clientName);

        return ResponseEntity.ok(balance);
    }
}
