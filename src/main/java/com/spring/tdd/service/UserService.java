package com.spring.tdd.service;

import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.model.UserDTO;

public interface UserService {
    UserDTO getUserByName(String name) throws UserNotFoundException;
    UserDTO saveUser(UserDTO userDTO);
}
