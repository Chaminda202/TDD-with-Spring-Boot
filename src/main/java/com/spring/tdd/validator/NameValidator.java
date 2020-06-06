package com.spring.tdd.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class NameValidator implements ConstraintValidator<Name, String> {
    // private MessageSource messageSource;
    List<String> validLists = Arrays.asList("Test", "Test 1", "Test 2");

    public NameValidator() {
    }

    /*public NameValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }*/

    public void initialize(Name constraintAnnotation) {
        // this.validLists = Arrays.stream(this.messageSource.getMessage("restricted.name.list", null, Locale.ENGLISH).split(",")).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return false;
        if (!validLists.contains(value))
            return true;
        return false;
    }
}
