package com.srmasset.ports.inbound.http.api.v1.controllers;

import com.srmasset.adapters.outbound.database.OutboundGameAdapter;
import com.srmasset.ports.inbound.http.api.v1.dto.OutboundStartGameDTO;
import com.srmasset.ports.inbound.http.api.v1.errors.ForbiddenException;
import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import com.srmasset.ports.outbound.database.user.UserDAO;
import datadog.trace.api.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
public class GameControllerApiV1 extends BaseController{

    @Autowired
    private OutboundGameAdapter outboundGameAdapter;

    @PostMapping("/start")
    @Trace(operationName = "GameControllerApiV1.start")
    public ResponseEntity<OutboundStartGameDTO> start() throws ForbiddenException {
        UserDAO user = this.authenticate();

        GameDAO gameDAO = outboundGameAdapter.createGameForUser(user);

        OutboundStartGameDTO outboundStartGameDTO = new OutboundStartGameDTO();
        outboundStartGameDTO.setGameId(gameDAO.getId());

        return ResponseEntity.ok(outboundStartGameDTO);
    }

    private void verifyOwner(GameDAO gameDAO) throws ForbiddenException {
        UserDAO user = this.authenticate();
        if (!gameDAO.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException();
        }
    }

    @GetMapping("/{gameId}/next_server_move")
    public void nextServerMove(@PathVariable Long gameId) throws ForbiddenException {
        GameDAO gameDAO = outboundGameAdapter.findGameById(gameId);
        verifyOwner(gameDAO);
    }

    @PostMapping("/{gameId}/move")
    public void move(@PathVariable Long gameId) throws ForbiddenException {
        GameDAO gameDAO = outboundGameAdapter.findGameById(gameId);
        verifyOwner(gameDAO);
    }

    @PostMapping("/{gameId}/end")
    public void end(@PathVariable Long gameId) throws ForbiddenException {
        GameDAO gameDAO = outboundGameAdapter.findGameById(gameId);
        verifyOwner(gameDAO);
    }
}
