package com.spring.tdd.validate;

import com.spring.tdd.TddDemoApplication;
import com.spring.tdd.common.CustomErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/***
 * Validate payload contains undefine attributes or invalid path variable
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TddDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SchemaValidate {
    @Autowired
    private TestRestTemplate testRestTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Test
    void testUndefineAttributeInPayload() throws Exception {
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestPayload = "{ \"age\": 24, \"birthday\": \"1995-11-25\", \"id\": 0, \"occupation\": \"Developer\", \"username\": \"Tom\", \"unknown\": \"unknown property\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<CustomErrorResponse> responseEntity = testRestTemplate.exchange(
                "/users",
                HttpMethod.POST,
                entity,
                CustomErrorResponse.class);

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(responseEntity.getBody().getDetails().size()).isPositive();
    }

    /***
     * Passing invalid data type as path variable
     */
    @Test
    public void testInvalidDataTypeInPathVariable () {
        //act
        ResponseEntity<CustomErrorResponse> responseEntity = testRestTemplate.exchange(
                "/users/age/{age}",
                HttpMethod.GET,
                null,
                CustomErrorResponse.class,
                "Invalid_Input");

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseEntity.getBody().getDetails().size()).isPositive();
    }

    /***
     * Call invalid endpoint
     */
    @Test
    public void testInvalidPath () {
        //act
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                "/invalid/age/{age}",
                HttpMethod.GET,
                null,
                String.class,
                "Invalid_Input");

        //assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
