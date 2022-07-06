package com.nikskul.kafkaspringbanking.dao;

import java.util.Map;
import java.util.Optional;

public interface BalanceDAO<ID, T> {

    void save(ID key, T value);

    Optional<T> getByKey(ID key);

    Map<ID, T> getAll();

}
