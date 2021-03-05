package com.pochka15.funfics.dto.form.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UsernameIsFree {
    String message() default "There already exists a user with this name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
