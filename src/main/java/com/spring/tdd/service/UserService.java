package com.spring.tdd.service;

import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.model.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserByName(String name) throws UserNotFoundException;
    UserDTO saveUser(UserDTO userDTO);
    List<UserDTO> getUsersByAge(Integer age) throws UserNotFoundException;
}
