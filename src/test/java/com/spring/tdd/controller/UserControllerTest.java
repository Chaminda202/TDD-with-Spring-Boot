package com.spring.tdd.controller;

import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test()
    public void saveUser_shouldReturnUser() throws Exception {
        UserDTO user = UserDTO.builder()
                .username("Tom")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        String requestPayload = "{ \"age\": 24, \"birthday\": \"1995-11-25\", \"id\": 0, \"occupation\": \"Developer\", \"username\": \"Tom\"}";

        given(this.userService
                .saveUser(user))
                .willReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content(requestPayload)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(CoreMatchers.notNullValue()));
    }

    @Test
    public void getUserByName_shouldReturnUser() throws Exception {
        given(this.userService
                .getUserByName(anyString()))
                .willReturn(UserDTO.builder()
                        .id(1)
                        .username("Tom")
                        .age(25)
                        .occupation("Developer")
                        .birthday(LocalDate.of(1995, 11, 24))
                        .build());

        this.mockMvc.perform(
                MockMvcRequestBuilders.
                    get("/users/{name}", "Tom"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("username").value("Tom"))
                    .andExpect(MockMvcResultMatchers.jsonPath("occupation").value("Developer"));
    }

    @Test
    public void getUserByName_notFoundUser() throws Exception {
        given(this.userService
                .getUserByName(anyString()))
                .willThrow(new UserNotFoundException("User not exist"));

        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/users/Jack"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
