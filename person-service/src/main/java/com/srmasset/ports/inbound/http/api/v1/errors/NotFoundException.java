package com.srmasset.ports.inbound.http.api.v1.errors;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message, "NotFound", 404);
    }
}
