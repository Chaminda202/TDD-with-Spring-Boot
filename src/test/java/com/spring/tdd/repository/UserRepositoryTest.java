package com.spring.tdd.repository;

import com.spring.tdd.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test()
    public void saveUser_shouldReturnUser() {
        User user = User.builder()
                .username("Tom")
                .age(25)
                .occupation("Developer")
                .build();

        User saveUser = this.userRepository.save(user);
        User retrieveUser = testEntityManager.find(User.class, saveUser.getId());

        Assertions.assertThat(saveUser.getUsername()).isEqualTo(retrieveUser.getUsername());
        Assertions.assertThat(saveUser.getOccupation()).isEqualTo(retrieveUser.getOccupation());
    }

    @Test
    public void getUserByName_shouldReturnUser() {
        User saveUser = this.testEntityManager.persistAndFlush(User.builder()
                .username("Tom")
                .age(25)
                .occupation("Developer")
                .build());
        Optional<User> optionalUser = this.userRepository.findByUsername("Tom");

        org.assertj.core.api.Assertions.assertThat(saveUser.getUsername()).isEqualTo(optionalUser.get().getUsername());
        Assertions.assertThat(saveUser.getOccupation()).isEqualTo(optionalUser.get().getOccupation());
    }
}
