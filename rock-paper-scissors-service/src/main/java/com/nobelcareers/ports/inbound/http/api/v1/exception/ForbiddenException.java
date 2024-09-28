package com.nobelcareers.ports.inbound.http.api.v1.exception;

public class ForbiddenException extends HttpException {
    public ForbiddenException() {
        super("Forbidden", "Forbidden", 403);
    }

    public ForbiddenException(String message) {
        super(message, "Forbidden", 403);
    }
}
