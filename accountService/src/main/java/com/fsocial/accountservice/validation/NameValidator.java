package com.fsocial.accountservice.validation;

import com.fsocial.accountservice.validation.constrain.NameValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class NameValidator implements ConstraintValidator<NameValid, String> {
    @Value("${app.validation.name-regex}")
    private String name_regex;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;
        return value.matches(name_regex);
    }

    @Override
    public void initialize(NameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
