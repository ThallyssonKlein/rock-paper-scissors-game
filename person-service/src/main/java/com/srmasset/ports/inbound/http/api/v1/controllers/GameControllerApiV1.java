package com.srmasset.ports.inbound.http.api.v1.controllers;

import com.srmasset.adapters.inbound.http.InboundGameAdapter;
import com.srmasset.adapters.outbound.database.OutboundGameAdapter;
import com.srmasset.ports.inbound.http.api.v1.dto.InboundGameResultDTO;
import com.srmasset.ports.inbound.http.api.v1.dto.OutboundGameResultDTO;
import com.srmasset.ports.inbound.http.api.v1.dto.OutboundServerMoveDTO;
import com.srmasset.ports.inbound.http.api.v1.dto.OutboundStartGameDTO;
import com.srmasset.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.srmasset.ports.inbound.http.api.v1.exception.NotFoundException;
import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import com.srmasset.ports.outbound.database.user.UserDAO;
import datadog.trace.api.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/game")
@Valid
public class GameControllerApiV1 extends BaseController{

    @Autowired
    private OutboundGameAdapter outboundGameAdapter;

    @Autowired
    private InboundGameAdapter inboundGameAdapter;

    @PostMapping("/start")
    @Trace(operationName = "GameControllerApiV1.start")
    public ResponseEntity<OutboundStartGameDTO> start() throws ForbiddenException {
        UserDAO user = this.authenticate();

        GameDAO gameDAO = this.outboundGameAdapter.createGameForUser(user);

        OutboundStartGameDTO outboundStartGameDTO = new OutboundStartGameDTO();
        outboundStartGameDTO.setGameId(gameDAO.getId());

        return ResponseEntity.ok(outboundStartGameDTO);
    }

    private UserDAO verifyOwner(GameDAO gameDAO) throws ForbiddenException {
        UserDAO user = this.authenticate();
        if (!gameDAO.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException();
        }
        return user;
    }

    @GetMapping("/{gameId}/next_server_move")
    public ResponseEntity<OutboundServerMoveDTO> nextServerMove(@PathVariable Long gameId) throws ForbiddenException, NotFoundException {
        GameDAO gameDAO = this.outboundGameAdapter.findGameById(gameId);
        UserDAO userDAO = verifyOwner(gameDAO);

        String hash = this.inboundGameAdapter.nextServerMove(gameId, userDAO.getId());

        OutboundServerMoveDTO outboundServerMoveDTO = new OutboundServerMoveDTO();
        outboundServerMoveDTO.setHash(hash);

        return ResponseEntity.ok(outboundServerMoveDTO);
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<OutboundGameResultDTO> move(@PathVariable Long gameId, @RequestBody InboundGameResultDTO inboundGameResultDTO) throws ForbiddenException, NotFoundException {
        GameDAO gameDAO = this.outboundGameAdapter.findGameById(gameId);
        verifyOwner(gameDAO);

        return ResponseEntity.ok(this.inboundGameAdapter.result(gameId, inboundGameResultDTO.getPlayerMovement().name()));
    }

    @PostMapping("/{gameId}/end")
    public ResponseEntity<Void> end(@PathVariable Long gameId) throws ForbiddenException, NotFoundException {
        GameDAO gameDAO = this.outboundGameAdapter.findGameById(gameId);
        verifyOwner(gameDAO);

        this.outboundGameAdapter.closeGame(gameId);

        return ResponseEntity.noContent().build();
    }
}
