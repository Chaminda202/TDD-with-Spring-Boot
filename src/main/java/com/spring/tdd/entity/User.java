package com.spring.tdd.entity;

import com.spring.tdd.validator.Age;
import com.spring.tdd.validator.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
@Age
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Name
    private String username;
    @Min(value = 1)
    private Integer age;
    private String occupation;
    @NotNull
    private LocalDate birthday;
}
