package com.spring.tdd.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomErrorResponse {
    private String message;
    private int statusCode;
}
