package com.fsocial.accountservice.validation.constrain;

import com.fsocial.accountservice.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { PasswordValidator.class })
public @interface PasswordValid {
    String message() default "INVALID_PASSWORD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
