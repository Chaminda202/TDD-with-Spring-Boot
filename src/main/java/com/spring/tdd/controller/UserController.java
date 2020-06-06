package com.spring.tdd.controller;

import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public UserDTO saveUser(@RequestBody @Valid UserDTO userDTO) {
        return this.userService.saveUser(userDTO);
    }
    @GetMapping(value = "{name}")
    public UserDTO getUserByName(@PathVariable String name) throws UserNotFoundException {
        return this.userService.getUserByName(name);
    }
}
