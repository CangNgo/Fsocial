package com.fsocial.accountservice.validation.constrain;

import com.fsocial.accountservice.validation.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { NameValidator.class })
public @interface NameValid {
    String message() default "INVALID_DOB";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
