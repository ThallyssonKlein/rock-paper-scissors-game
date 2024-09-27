package com.srmasset.ports.inbound.http.api.v1.errors;

public class InternalErrorException extends HttpException {
    public InternalErrorException(String message) {
        super(message, "InternalError", 500);
    }
}
