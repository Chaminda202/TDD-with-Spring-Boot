package com.spring.tdd.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = AgeValidator.class)
public @interface Age {
    String message() default "{age.annotation.msg}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
