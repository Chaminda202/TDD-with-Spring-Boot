package com.spring.tdd.controller;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return this.userService.saveUser(user);
    }

    @GetMapping(value = "{name}")
    public User getUserByName(@PathVariable String name) throws UserNotFoundException {
        return this.userService.getUserByName(name);
    }
}
