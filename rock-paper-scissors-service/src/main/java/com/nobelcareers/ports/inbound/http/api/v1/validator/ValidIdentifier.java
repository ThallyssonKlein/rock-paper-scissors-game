package com.nobelcareers.ports.inbound.http.api.v1.validator;

import com.nobelcareers.ports.inbound.http.api.v1.validator.impl.ValidIdentifierImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidIdentifierImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIdentifier {
    String message() default "Identifier must have exactly 8, 10, 11 or 14 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}