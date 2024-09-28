package com.nobelcareers.ports.inbound.http.api.v1.validator.impl;

import com.nobelcareers.ports.inbound.http.api.v1.validator.ValidDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class ValidDateImpl implements ConstraintValidator<ValidDate, String> {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private String fieldName;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            FORMATTER.parse(value);
            return true;
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid date format for field: " + fieldName)
                    .addConstraintViolation();
            return false;
        }
    }
}