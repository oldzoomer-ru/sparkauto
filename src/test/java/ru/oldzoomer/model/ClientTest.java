package ru.oldzoomer.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
public class ClientTest {

    private final Validator validator;

    public ClientTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidClient() {
        Client client = new Client();
        client.setName("Иван");
        client.setSurname("Иванов");
        client.setEmail("ivan@example.com");
        client.setPhone("+79991234567");

        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidClientMissingName() {
        Client client = new Client();
        client.setSurname("Иванов");
        client.setEmail("ivan@example.com");

        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidClientInvalidEmail() {
        Client client = new Client();
        client.setName("Иван");
        client.setSurname("Иванов");
        client.setEmail("invalid-email");

        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testInvalidClientInvalidPhone() {
        Client client = new Client();
        client.setName("Иван");
        client.setSurname("Иванов");
        client.setPhone("123");

        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phone")));
    }
}