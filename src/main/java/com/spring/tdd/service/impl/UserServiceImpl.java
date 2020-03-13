package com.spring.tdd.service.impl;

import com.spring.tdd.entity.User;
import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.repository.UserRepository;
import com.spring.tdd.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Cacheable(cacheNames = "users", key = "#name")
    @Override
    @Transactional
    public User getUserByName(String name) throws UserNotFoundException {
        return this.userRepository
                .findByUsername(name)
                .orElseThrow(() -> new UserNotFoundException("User is not exist " + name));
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }
}
