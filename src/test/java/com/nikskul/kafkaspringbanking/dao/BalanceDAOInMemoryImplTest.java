package com.nikskul.kafkaspringbanking.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BalanceDAOInMemoryImpl.class)
@DirtiesContext
class BalanceDAOInMemoryImplTest {

    @Autowired
    BalanceDAOInMemoryImpl dao;

    @Test
    void shouldSave() {

        var key = "test";
        var expected = BigDecimal.TEN;

        dao.save(key, expected);

        assertTrue(dao.getByKey(key).isPresent());
        assertEquals(expected, dao.getByKey(key).get());
    }

    @Test
    void shouldReturnOptionalEmpty() {

        var key = "empty";

        assertNotNull(dao.getByKey(key));
        assertEquals(Optional.empty(), dao.getByKey(key));

    }

    @Test
    void shouldReturnNotNullOptional() {

        var key = "notEmpty";
        var expected = BigDecimal.TEN;

        dao.save(key, expected);

        assertTrue(dao.getByKey(key).isPresent());

    }

    @Test
    void shouldReturnNotNullMap() {
        assertNotNull(dao.getAll());
    }
}