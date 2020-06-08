package com.spring.tdd.validate;

import com.spring.tdd.exception.BusinessValidationException;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.service.UserBusinessValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserBusinessValidate {
    @Autowired
    private MessageSource messageSource;
    private UserBusinessValidation userBusinessValidation;

    @BeforeEach
    public void setUp() {
        this.userBusinessValidation = new UserBusinessValidation(this.messageSource);
    }

    @Test
    void testUserEligibleDiscount() {
        UserDTO user = UserDTO.builder()
                .username("Test")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        Throwable exception = assertThrows(BusinessValidationException.class, () -> this.userBusinessValidation.businessCriteria(user));
        assertEquals(exception.getMessage(), "User does not eligible for discount");;
    }

    @Test
    void testUserOccupationInCurrentList() {
        UserDTO user = UserDTO.builder()
                .username("Test")
                .age(37)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        Throwable exception = assertThrows(BusinessValidationException.class, () -> this.userBusinessValidation.businessCriteria(user));
        assertEquals(exception.getMessage(), "User's occupation does not in the current list");
    }

    @Test
    void testBusinessValidationSuccess() {
        UserDTO user = UserDTO.builder()
                .username("Test")
                .age(37)
                .occupation("Manager")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        this.userBusinessValidation.businessCriteria(user);
    }
}
