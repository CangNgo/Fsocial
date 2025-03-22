package com.fsocial.profileservice.validation.constrain;

import com.fsocial.profileservice.validation.NotNullOrBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { NotNullOrBlankValidator.class })
public @interface NotNullOrBlank {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
