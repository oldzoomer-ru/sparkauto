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
public class WorkTest {

    private final Validator validator;

    public WorkTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void testValidWork() {
        Work work = new Work();
        work.setName("Ремонт двигателя");
        work.setNormalHours(10.0);
        work.setPricePerHour(1500.0);

        Set<ConstraintViolation<Work>> violations = validator.validate(work);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidWorkMissingName() {
        Work work = new Work();
        work.setNormalHours(10.0);
        work.setPricePerHour(1500.0);

        Set<ConstraintViolation<Work>> violations = validator.validate(work);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    public void testInvalidWorkNegativeHours() {
        Work work = new Work();
        work.setName("Ремонт двигателя");
        work.setNormalHours(-5.0);
        work.setPricePerHour(1500.0);

        Set<ConstraintViolation<Work>> violations = validator.validate(work);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("normalHours")));
    }

    @Test
    public void testInvalidWorkNegativePrice() {
        Work work = new Work();
        work.setName("Ремонт двигателя");
        work.setNormalHours(10.0);
        work.setPricePerHour(-1500.0);

        Set<ConstraintViolation<Work>> violations = validator.validate(work);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("pricePerHour")));
    }
}