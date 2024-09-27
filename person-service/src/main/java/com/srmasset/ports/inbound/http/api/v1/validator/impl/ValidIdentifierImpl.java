package com.srmasset.ports.inbound.http.api.v1.validator.impl;

import com.srmasset.ports.inbound.http.api.v1.validator.ValidIdentifier;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidIdentifierImpl implements ConstraintValidator<ValidIdentifier, String> {
    @Override
    public void initialize(ValidIdentifier constraintAnnotation) {
    }

    @Override
    public boolean isValid(String identifier, ConstraintValidatorContext context) {
        int length = identifier.length();
        boolean isNumeric = identifier.chars().allMatch(Character::isDigit);

        if (!isNumeric) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Identifier must be numeric")
                    .addConstraintViolation();
            return false;
        }

        if (length == 8) {
            int firstDigit = Character.getNumericValue(identifier.charAt(0));
            int lastDigit = Character.getNumericValue(identifier.charAt(7));
            if (firstDigit + lastDigit != 9) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("For identifiers of length 8, the sum of the first and last digit must be 9")
                        .addConstraintViolation();
                return false;
            }
        }

        if (length == 10) {
            char lastDigit = identifier.charAt(9);
            if (identifier.substring(0, 9).indexOf(lastDigit) != -1) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("For identifiers of length 10, the last digit must not be present in the other 9 digits")
                        .addConstraintViolation();
                return false;
            }
        }

        if (!(length == 8 || length == 10 || length == 11 || length == 14)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Identifier must have exactly 8, 10, 11 or 14 characters")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}