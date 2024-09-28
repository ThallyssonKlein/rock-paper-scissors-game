package com.srmasset.adapters.outbound.database;

import com.srmasset.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.srmasset.ports.outbound.database.user.OutboundUserRepositoryPort;
import com.srmasset.ports.outbound.database.user.UserDAO;
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
