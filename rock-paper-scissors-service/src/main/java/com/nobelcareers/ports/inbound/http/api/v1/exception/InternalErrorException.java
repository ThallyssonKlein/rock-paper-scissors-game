package com.nobelcareers.ports.inbound.http.api.v1.exception;

public class InternalErrorException extends HttpException {
    public InternalErrorException(String message) {
        super(message, "InternalError", 500);
    }
}
