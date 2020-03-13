package com.spring.tdd.service;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.repository.UserRepository;
import com.spring.tdd.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeAll
    public void setUp() {
        this.userService = new UserServiceImpl(this.userRepository);
    }

    @Test
    public void getUserByName_shouldReturnUser() {
        given(this.userRepository
                .findByUsername(anyString()))
                .willReturn(Optional.of(User.builder()
                        .id(1)
                        .username("Tom")
                        .age(25)
                        .occupation("Developer")
                        .build()));

        //act
        User user = this.userService.getUserByName("Tom");

        Assertions.assertThat(user.getUsername()).isEqualTo("Tom");
        Assertions.assertThat(user.getOccupation()).isEqualTo("Developer");

    }

    @Test()
    public void getUserByName_notFoundUser() throws Exception {
        given(this.userRepository
                .findByUsername(anyString()))
                .willThrow(new UserNotFoundException("User not exist"));

        Assertions.assertThatThrownBy(() -> {
            this.userService.getUserByName("Tom");
        }).isInstanceOf(UserNotFoundException.class)
               .hasMessage("User not exist");

        /*Throwable thrown = Assertions.catchThrowable(() -> {
            this.userService.getUserByName("Tom");
        });
        Assertions.assertThat(thrown).isNotNull().isInstanceOf(UserNotFoundException.class);*/
    }
}
