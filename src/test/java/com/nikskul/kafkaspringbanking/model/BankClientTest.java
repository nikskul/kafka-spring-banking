package com.nikskul.kafkaspringbanking.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BankClientTest {

    @Test
    void serialization() {
//        String json = "{\"name\":\"Frank\",\"id\":5000}";
//        BankClient employee = new ObjectMapper().readValue(json, Employee.class);
//
//        assertEquals("Frank", employee.getName());
//        assertEquals(5000, employee.getId());
    }

    @Test
    void mustCreateNewClient() {

        BankClient client = new BankClient(UUID.randomUUID(), "FirstName", "LastName", "SecondName");

        assertEquals("FirstName", client.getFirstName());
        assertEquals("LastName", client.getLastName());
        assertEquals("SecondName", client.getSecondName());

    }

    @Test
    void mustThrowIllegalArgumentExceptionWhenCreateClient() {

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new BankClient(UUID.randomUUID(),"    ", "LastName", "SecondName");
                }
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new BankClient(UUID.randomUUID(),"FirstName", "    ", "SecondName");
                }
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new BankClient(UUID.randomUUID(),"FirstName", "LastName", "    ");
                }
        );
    }

    @Test
    void mustThrowIllegalArgumentExceptionWhenUseSetterForClientName() {

        BankClient client = new BankClient(UUID.randomUUID(),"FirstName", "LastName", "SecondName");

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    client.setFirstName("   ");
                }
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    client.setSecondName("   ");
                }
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    client.setLastName("   ");
                }
        );

    }

}