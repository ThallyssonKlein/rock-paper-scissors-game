package com.nobelcareers.ports.inbound.http.api.v1.exception;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super("Not found", "NotFound", 404);
    }

    public NotFoundException(String message) {
        super(message, "NotFound", 404);
    }
}
