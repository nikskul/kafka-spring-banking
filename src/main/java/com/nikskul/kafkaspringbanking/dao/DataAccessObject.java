package com.nikskul.kafkaspringbanking.dao;

import java.util.Optional;

public interface DataAccessObject<ID, T> {

    void save(ID key, T value);

    Optional<T> getByKey(ID key);

}
