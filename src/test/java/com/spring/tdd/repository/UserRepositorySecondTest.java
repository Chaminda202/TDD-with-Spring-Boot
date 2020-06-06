package com.spring.tdd.repository;

import com.spring.tdd.entity.User;
import com.spring.tdd.validator.NameValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositorySecondTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUser_shouldReturnUser() {
        User user = User.builder()
                .username("Tom")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build();

        User saveUser = this.userRepository.save(user);
        User retrieveUser = userRepository.findById(saveUser.getId()).get();

        Assertions.assertThat(saveUser.getUsername()).isEqualTo(retrieveUser.getUsername());
        Assertions.assertThat(saveUser.getOccupation()).isEqualTo(retrieveUser.getOccupation());
    }

    @Test
    public void getUserByName_shouldReturnUser() {
        User saveUser = this.userRepository.save(User.builder()
                .username("Tim")
                .age(24)
                .occupation("Developer")
                .birthday(LocalDate.of(1995, 11, 24))
                .build());
        Optional<User> optionalUser = this.userRepository.findByUsername("Tim");

        Assertions.assertThat(saveUser.getUsername()).isEqualTo(optionalUser.get().getUsername());
        Assertions.assertThat(saveUser.getOccupation()).isEqualTo(optionalUser.get().getOccupation());
    }
}
