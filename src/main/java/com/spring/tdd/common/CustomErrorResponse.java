package com.spring.tdd.common;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomErrorResponse {
    private List<String> details;
    private int statusCode;
    private String errorCode;
}
