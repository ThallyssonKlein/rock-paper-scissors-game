package com.srmasset.ports.inbound.http.api.v1.controllers;

import com.srmasset.adapters.outbound.database.OutboundUserAdapter;
import com.srmasset.adapters.outbound.database.exception.UserNotFoundException;
import com.srmasset.ports.inbound.http.api.v1.errors.ForbiddenException;
import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import com.srmasset.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
public class GameControllerApiV1 {

    @Autowired
    private OutboundUserAdapter outboundUserAdapter;

    @PostMapping("/start")
    public void start() {}

    private void verifyOwner(GameDAO gameDAO) throws ForbiddenException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            UserDAO user = outboundUserAdapter.findByUsername(username);
            if (!gameDAO.getOwner().getId().equals(user.getId())) {
                throw new ForbiddenException();
            }
        } catch (UserNotFoundException e) {
            throw new ForbiddenException();
        }
    }

    @GetMapping("/{gameId}/next_server_move")
    public void nextServerMove() {
        verifyOwner();
    }

    @PostMapping("/{gameId}/move")
    public void move() {
        verifyOwner();
    }

    @PostMapping("/{gameId}/end")
    public void end() {
        verifyOwner();
    }
}
