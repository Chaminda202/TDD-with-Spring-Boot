package com.spring.tdd.validate;

import com.spring.tdd.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserEntityValidate {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserNameNull() {
        User user = User.builder()
                .username(null)
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        for (ConstraintViolation<User> violation : constraintViolations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(constraintViolations.size(), 2);
        assertFalse(constraintViolations.isEmpty());
    }

    @Test
    void testUserNameNotNullAndRestricted() {
        User user = User.builder()
                .username("Test")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        for (ConstraintViolation<User> violation : constraintViolations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(constraintViolations.size(), 1);
        assertFalse(constraintViolations.isEmpty());
    }

    @Test
    void testInvalidAge() {
        User user = User.builder()
                .username("Te")
                .age(0)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        for (ConstraintViolation<User> violation : constraintViolations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(constraintViolations.size(), 2);
        assertFalse(constraintViolations.isEmpty());
    }

    @Test
    void testViolateAgeCondition() {
        User user = User.builder()
                .username("Te")
                .age(20)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        for (ConstraintViolation<User> violation : constraintViolations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(constraintViolations.size(), 1);
        assertFalse(constraintViolations.isEmpty());
    }
}
