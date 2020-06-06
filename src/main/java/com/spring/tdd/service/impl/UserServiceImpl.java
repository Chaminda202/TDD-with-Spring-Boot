package com.spring.tdd.service.impl;

import com.spring.tdd.exception.UserNotFoundException;
import com.spring.tdd.mapper.UserMapper;
import com.spring.tdd.model.UserDTO;
import com.spring.tdd.repository.UserRepository;
import com.spring.tdd.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    @Override
    @Transactional
    public UserDTO getUserByName(String name) throws UserNotFoundException {
        return this.userRepository
                .findByUsername(name)
                .map(this.userMapper::mapUserToUserDto)
                .orElseThrow(() -> new UserNotFoundException("User is not exist " + name));
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        return this.userMapper
                .mapUserToUserDto(this.userRepository.save(this.userMapper.mapUserDtoToUserEntity(userDTO)));
    }
}
