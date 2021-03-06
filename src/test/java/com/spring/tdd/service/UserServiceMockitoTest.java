package com.spring.tdd.service;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.repository.UserRepository;
import com.spring.tdd.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockitoTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        this.userService = new UserServiceImpl(this.userRepository);
    }

    @Test()
    public void saveUser_shouldReturnUser() throws Exception {
        User user = User.builder()
                .username("Tom")
                .age(25)
                .occupation("Developer")
                .build();

        given(this.userRepository
                .save(user))
                .willReturn(user);

        //act
        User saveUser = this.userService.saveUser(user);

        Assertions.assertThat(user.getUsername()).isEqualTo(saveUser.getUsername());
        Assertions.assertThat(user.getOccupation()).isEqualTo(saveUser.getOccupation());
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
    }
}
