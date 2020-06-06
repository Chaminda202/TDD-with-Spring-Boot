package com.spring.tdd.service;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.mapper.UserMapper;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.repository.UserRepository;
import com.spring.tdd.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        this.userService = new UserServiceImpl(this.userRepository, this.userMapper);
    }

    @Test
    @Order(1)
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
        UserDTO user = this.userService.getUserByName("Tom");

        Assertions.assertThat(user.getUsername()).isEqualTo("Tom");
        Assertions.assertThat(user.getOccupation()).isEqualTo("Developer");

    }

    //@Test
    //@Order(2)
    public void getUserByName_notFoundUser() throws Exception {
        /*given(this.userRepository
                .findByUsername(anyString()))
                .willThrow(new UserNotFoundException("User is not exist Tom1"));

        Assertions.assertThatThrownBy(() -> {
            this.userService.getUserByName("Tom1");
        }).isInstanceOf(UserNotFoundException.class);
               // .hasMessage("User is not exist Tom1");
*/
        Throwable exception = assertThrows(UserNotFoundException.class, () -> this.userService.getUserByName("Tom1"));
        assertEquals(exception.getMessage(), "User is not exist Tom1");
    }
}
