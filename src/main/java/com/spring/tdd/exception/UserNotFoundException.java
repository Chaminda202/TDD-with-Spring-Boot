package com.spring.tdd.exception;
// @ResponseStatus(value = HttpStatus.NOT_FOUND) without adding global exception handler
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
