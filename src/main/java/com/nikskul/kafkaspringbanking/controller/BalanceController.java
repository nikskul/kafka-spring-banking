package com.nikskul.kafkaspringbanking.controller;

import com.nikskul.kafkaspringbanking.model.User;
import com.nikskul.kafkaspringbanking.request.CredentialsRequest;
import com.nikskul.kafkaspringbanking.service.UserService;
import com.nikskul.kafkaspringbanking.service.balance.UserBankAccountBalanceService;
import com.nikskul.kafkaspringbanking.service.user.SimpleUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/public/balances")
public class BalanceController {

    private final SimpleUserService userService;

    private final UserBankAccountBalanceService balanceService;

    public BalanceController(
            UserBankAccountBalanceService balanceService,
            SimpleUserService userService
    ) {
        this.balanceService = balanceService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<BigDecimal> getBalance(
            @RequestBody final CredentialsRequest request
    ) {

        userService.authenticate(request);

        var user = userService.getByUsername(request.username());

        BigDecimal balance = balanceService.getBalance(user);

        return ResponseEntity.ok(balance);
    }
}
