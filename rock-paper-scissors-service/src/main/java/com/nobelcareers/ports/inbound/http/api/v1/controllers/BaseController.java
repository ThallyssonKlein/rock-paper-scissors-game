package com.nobelcareers.ports.inbound.http.api.v1.controllers;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundUserAdapter;
import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.inbound.http.api.v1.exception.NotFoundException;
import com.nobelcareers.ports.outbound.database.game.dao.GameDAO;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class BaseController {

    @Autowired
    protected OutboundUserAdapter outboundUserAdapter;

    @Autowired
    protected OutboundGameAdapter outboundGameAdapter;

    protected UserDAO authenticate() throws ForbiddenException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException();
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        UserDAO user = new UserDAO();
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());

        return user;
    }

    protected UserDAO verifyOwner(Long gameId) throws ForbiddenException, NotFoundException {
        GameDAO gameDAO = this.outboundGameAdapter.findGameById(gameId);

        UserDAO user = this.authenticate();
        if (!gameDAO.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException();
        }
        return user;
    }
}