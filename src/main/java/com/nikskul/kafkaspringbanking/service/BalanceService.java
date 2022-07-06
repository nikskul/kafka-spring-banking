package com.nikskul.kafkaspringbanking.service;

import java.math.BigDecimal;

public interface BalanceService {

    BigDecimal findBalanceById(String id);

}
