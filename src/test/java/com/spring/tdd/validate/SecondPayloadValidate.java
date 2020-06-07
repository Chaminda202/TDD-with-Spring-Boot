package com.spring.tdd.validate;

import com.spring.tdd.TddDemoApplication;
import com.spring.tdd.common.CustomErrorResponse;
import com.spring.tdd.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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
