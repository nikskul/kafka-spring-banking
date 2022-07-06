package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.request.OperationRequest;

import java.math.BigDecimal;

public interface BalanceService {

    BigDecimal findBalanceById(String id);

    void makeDeposit(OperationRequest request);

    void makeWithdrawal(OperationRequest request);

}
