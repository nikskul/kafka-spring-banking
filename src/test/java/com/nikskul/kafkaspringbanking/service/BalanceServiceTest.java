package com.nikskul.kafkaspringbanking.service;

import com.nikskul.kafkaspringbanking.dao.BalanceDAOInMemoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    BalanceDAOInMemoryImpl dao;

    @InjectMocks
    BalanceServiceImpl service;

    @Test
    void shouldReturnValue() {
        String key = "ivanRich";
        BigDecimal expected = BigDecimal.valueOf(100_000);

        Mockito.when(dao.getByKey(key)).thenReturn(Optional.of(expected));

        var balance = service.findBalanceById(key);

        assertEquals(expected, balance);
    }

    @Test
    void shouldReturnZero() {
        String key = "ivanRich";
        BigDecimal expected = BigDecimal.ZERO;

        Mockito.when(dao.getByKey(key)).thenReturn(Optional.empty());

        var balance = service.findBalanceById(key);

        assertEquals(expected, balance);
    }

}