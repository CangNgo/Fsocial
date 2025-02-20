package com.fsocial.accountservice.validation;

import com.fsocial.accountservice.validation.constrain.PasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

    @Value("${app.validation.password-regex}")
    private String passwordRegex;

    @Value("${app.validation.password-length}")
    private int passwordLength;

    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.length() >= passwordLength && value.matches(passwordRegex);
    }
}
