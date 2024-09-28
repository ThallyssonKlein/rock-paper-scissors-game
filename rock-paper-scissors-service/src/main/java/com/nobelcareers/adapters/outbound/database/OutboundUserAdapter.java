package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.outbound.database.user.OutboundUserRepositoryPort;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboundUserAdapter {

    @Autowired
    private OutboundUserRepositoryPort outboundUserRepositoryPort;

    public UserDAO findByUsername(String username) throws ForbiddenException {
        return outboundUserRepositoryPort.findByUsername(username).orElseThrow(() -> new ForbiddenException("User not found"));
    }
}
