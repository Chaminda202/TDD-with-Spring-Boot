package com.spring.tdd;

import com.spring.tdd.model.UserDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import com.spring.tdd.entity.User;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    public void saveUser_shouldReturnUser() {
        UserDTO user = UserDTO.builder()
                .username("Tom")
                .age(40)
                .occupation("Manager")
                .birthday(LocalDate.of(1980, 1, 24))
                .build();
        //act
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> entity = new HttpEntity<>(user, headers);

        ResponseEntity<UserDTO> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.POST,
                entity,
                UserDTO.class);

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getUsername()).isEqualTo(user.getUsername());
        assertThat(responseEntity.getBody().getOccupation()).isEqualTo(user.getOccupation());
    }

    @Test
    @Order(2)
    public void getUserByName_shouldReturnUser () {

        //act
        ResponseEntity<UserDTO> responseEntity = testRestTemplate.exchange(
                "/users/{name}",
                HttpMethod.GET,
                null,
                UserDTO.class,
                "Tom");

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getUsername()).isEqualTo("Tom");
        assertThat(responseEntity.getBody().getOccupation()).isEqualTo("Manager");
    }
}
