package com.nobelcareers.domain.person.error;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InvalidIdentifierException extends RuntimeException {
    private final String name = "InvalidIdentifier";

    public InvalidIdentifierException(String message) {
        super(message);
    }
}
