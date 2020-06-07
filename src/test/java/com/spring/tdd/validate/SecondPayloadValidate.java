package com.spring.tdd.validate;

import com.spring.tdd.TddDemoApplication;
import com.spring.tdd.common.CustomErrorResponse;
import com.spring.tdd.controller.UserController;
import com.spring.tdd.handler.CustomGlobalExceptionHandler;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TddDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecondPayloadValidate {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    HttpHeaders headers = new HttpHeaders();

    @Test
    void testUserNameNull() throws Exception {
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO user = UserDTO.builder()
                .username(null)
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);

        ResponseEntity<CustomErrorResponse> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.POST,
                entity,
                CustomErrorResponse.class);

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseEntity.getBody().getDetails()).contains("Invalid user name");
    }

   @Test
    void testUserNameNotNullAndRestricted() {
       headers.setContentType(MediaType.APPLICATION_JSON);
       UserDTO user = UserDTO.builder()
               .username("Test")
               .age(24)
               .occupation("Developer")
               .birthday(LocalDate.of(1995, 11, 24))
               .build();

       HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);

       ResponseEntity<CustomErrorResponse> responseEntity = testRestTemplate.exchange(
               "/users",
               HttpMethod.POST,
               entity,
               CustomErrorResponse.class);

       //assert
       assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
       assertThat(responseEntity.getBody().getDetails()).contains("Invalid user name");
    }

    @Test
    void testInvalidAge() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO user = UserDTO.builder()
                .username("Test eee")
                .age(15)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);

        ResponseEntity<CustomErrorResponse> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.POST,
                entity,
                CustomErrorResponse.class);

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseEntity.getBody().getDetails()).contains("Age should be greater than 18");
        assertThat(responseEntity.getBody().getDetails()).contains("Age and Birthday fields validation failed");
    }

    @Test
    void testViolateAgeCondition() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserDTO user = UserDTO.builder()
                .username("Test eeee")
                .age(20)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);

        ResponseEntity<CustomErrorResponse> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.POST,
                entity,
                CustomErrorResponse.class);

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseEntity.getBody().getDetails()).contains("Age and Birthday fields validation failed");
    }
}
