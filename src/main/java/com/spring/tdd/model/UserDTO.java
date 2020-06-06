package com.spring.tdd.model;

import com.spring.tdd.validator.Age;
import com.spring.tdd.validator.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@Age(message = "{age.validation.msg}")
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    @NotNull
    @Name
    private String username;
    @NotNull
    private Integer age;
    private String occupation;
    @NotNull
    private LocalDate birthday;
}
