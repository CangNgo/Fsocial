package com.fsocial.accountservice.validation;

import com.fsocial.accountservice.validation.constrain.DobValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobValid, LocalDate> {

    @Value("${app.validation.age-valid}")
    private int age_valid;

    @Override
    public void initialize(DobValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;

        long age = ChronoUnit.YEARS.between(value, LocalDate.now());

        return age >= age_valid;
    }
}
