package com.nobelcareers.ports.inbound.http.api.v1.validator;

import com.nobelcareers.ports.inbound.http.api.v1.validator.impl.ValidDateImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDateImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default "Invalid date format for field: ";
    String fieldName() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}