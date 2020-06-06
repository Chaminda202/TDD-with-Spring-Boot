package com.spring.tdd.validator;

import com.spring.tdd.entity.User;
import com.spring.tdd.model.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, Object> {
    @Override
    public void initialize(Age constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof User) {
            Period between = Period.between(((User) value).getBirthday(), LocalDate.now());
            return ((User) value).getAge() == between.getYears();
        }else if(value instanceof UserDTO) {
            Period between = Period.between(((UserDTO) value).getBirthday(), LocalDate.now());
            return ((UserDTO) value).getAge() == between.getYears();
        }
        return false;
    }
}
