package com.rocketpaperscissors.adapters.outbound.database;

import com.rocketpaperscissors.ports.outbound.database.user.OutboundUserRepositoryPort;
import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
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
