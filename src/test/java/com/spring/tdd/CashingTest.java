package com.spring.tdd;

import com.spring.tdd.entity.User;
import com.spring.tdd.repository.UserRepository;
import com.spring.tdd.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
public class CashingTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test()
    public void getUserByName_shouldReturnCashedUser() {
        given(this.userRepository
                .findByUsername("Tom"))
                .willReturn(Optional.of(User.builder()
                        .id(1)
                        .username("Tom")
                        .age(25)
                        .occupation("Developer")
                        .build()));

        this.userService.getUserByName("Tom");
        this.userService.getUserByName("Tom");
        verify(this.userRepository, times(1)).findByUsername("Tom");
    }
}
