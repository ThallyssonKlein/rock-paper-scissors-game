package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.outbound.database.user.OutboundUserRepositoryPort;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OutboundUserAdapter {

    @Autowired
    private OutboundUserRepositoryPort outboundUserRepositoryPort;

    public Optional<UserDAO> findByUsername(String username) {
        return outboundUserRepositoryPort.findByUsername(username);
    }
}
