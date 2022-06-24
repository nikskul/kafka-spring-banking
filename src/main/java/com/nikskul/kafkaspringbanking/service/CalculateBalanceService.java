package com.nikskul.kafkaspringbanking.service;

import org.springframework.stereotype.Service;

@Service
public class CalculateBalanceService {

    public void calculate() {
        System.out.println("CalculateBalanceService called.\nCalculating balance...\nDone.");
    }

}
