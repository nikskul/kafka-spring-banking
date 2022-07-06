package com.nikskul.kafkaspringbanking.dao;

import com.nikskul.kafkaspringbanking.request.OperationRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public interface BalanceDAO<ID, T> {

    void save(ID key, T value);

    Optional<T> getByKey(ID key);

}
