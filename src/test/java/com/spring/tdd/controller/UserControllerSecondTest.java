package com.spring.tdd.controller;

import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.handler.CustomGlobalExceptionHandler;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserControllerSecondTest {
    @Mock
    private UserService userService;
    private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                        .setControllerAdvice(new CustomGlobalExceptionHandler())
                        .build();
    }

    @Test
    public void saveUser_shouldReturnUser() throws Exception {
        UserDTO user = UserDTO.builder()
                .username("Tom")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        String requestPayload = "{ \"age\": 24, \"birthday\": \"1995-11-25\", \"id\": 0, \"occupation\": \"Developer\", \"username\": \"Tom\"}";

        given(userService.saveUser(any())).willReturn(user);

        this.mockMvc
                .perform(post("/users")
                .content(requestPayload)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(CoreMatchers.equalTo(user.getUsername())))
                .andExpect(jsonPath("$.occupation").value(CoreMatchers.equalTo(user.getOccupation())))
                .andExpect(jsonPath("$.age").value(CoreMatchers.equalTo(user.getAge())))
                .andExpect(content().string(CoreMatchers.notNullValue()));
    }

    @Test
    public void getUserByName_shouldReturnUser() throws Exception {
        UserDTO user = UserDTO.builder()
                .username("Tom")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        given(this.userService
                .getUserByName(any()))
                .willReturn(user);

        this.mockMvc
                .perform(get("/users/{name}", "Tom").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(CoreMatchers.equalTo(user.getUsername())))
                .andExpect(jsonPath("$.occupation").value(CoreMatchers.equalTo(user.getOccupation())))
                .andExpect(jsonPath("$.age").value(CoreMatchers.equalTo(user.getAge())));
    }

    @Test
    public void getUserByName_notFoundUser() throws Exception {
        given(this.userService
                .getUserByName(any()))
                .willThrow(new UserNotFoundException("User not exist"));

        this.mockMvc
                .perform(get("/users/{name}", "Test"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(CoreMatchers.equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.details").value(CoreMatchers.hasItem("User not exist")));
    }
}
