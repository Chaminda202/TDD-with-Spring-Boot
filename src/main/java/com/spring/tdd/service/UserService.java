package com.spring.tdd.service;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;

public interface UserService {
    User getUserByName(String name) throws UserNotFoundException;
    User saveUser(User user);
}
