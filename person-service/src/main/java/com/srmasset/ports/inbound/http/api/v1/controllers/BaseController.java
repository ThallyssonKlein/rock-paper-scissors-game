package com.srmasset.ports.inbound.http.api.v1.controllers;

import com.srmasset.adapters.outbound.database.OutboundUserAdapter;
import com.srmasset.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.srmasset.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    @Autowired
    protected OutboundUserAdapter outboundUserAdapter;

    protected UserDAO authenticate() throws ForbiddenException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return outboundUserAdapter.findByUsername(username);
    }
}