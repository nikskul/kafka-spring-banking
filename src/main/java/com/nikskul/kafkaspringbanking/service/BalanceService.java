package com.nikskul.kafkaspringbanking.service;

import java.math.BigDecimal;

public interface BalanceService<K> {

    BigDecimal getBalance(K key);

}
