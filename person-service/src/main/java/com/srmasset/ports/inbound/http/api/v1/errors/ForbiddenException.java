package com.srmasset.ports.inbound.http.api.v1.errors;

public class ForbiddenException extends HttpException {
    public ForbiddenException() {
        super("Forbidden", "Forbidden", 403);
    }
}
