package com.spring.tdd.controller;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.service.UserService;
import com.spring.tdd.util.JacksonUtil;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test()
    public void saveUser_shouldReturnUser() throws Exception {
        User user = User.builder()
                .username("Tom")
                .age(25)
                .occupation("Developer")
                .build();

        given(this.userService
                .saveUser(user))
                .willReturn(user);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .content(JacksonUtil.convertObjectToJson(user))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("occupation").value(user.getOccupation()));
    }

    @Test
    public void getUserByName_shouldReturnUser() throws Exception {
        given(this.userService
                .getUserByName(anyString()))
                .willReturn(User.builder()
                        .id(1)
                        .username("Tom")
                        .age(25)
                        .occupation("Developer")
                        .build());

        this.mockMvc.perform(
                MockMvcRequestBuilders.
                    get("/users/Tom"))
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
