package com.srmasset.adapters.outbound.database;

import com.srmasset.adapters.outbound.database.exception.UserNotFoundException;
import com.srmasset.ports.outbound.database.user.OutboundUserRepositoryPort;
import com.srmasset.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboundUserAdapter {

    @Autowired
    private OutboundUserRepositoryPort outboundUserRepositoryPort;

    public UserDAO findByUsername(String username) {
        return outboundUserRepositoryPort.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
