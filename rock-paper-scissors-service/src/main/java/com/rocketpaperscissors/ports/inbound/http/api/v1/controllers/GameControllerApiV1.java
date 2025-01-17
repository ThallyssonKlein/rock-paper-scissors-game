package com.rocketpaperscissors.ports.inbound.http.api.v1.controllers;

import com.rocketpaperscissors.adapters.inbound.http.api.v1.InboundGameAdapter;
import com.rocketpaperscissors.ports.inbound.http.api.v1.dto.InboundGameResultDTO;
import com.rocketpaperscissors.ports.inbound.http.api.v1.dto.OutboundGameResultDTO;
import com.rocketpaperscissors.ports.inbound.http.api.v1.dto.OutboundServerMoveDTO;
import com.rocketpaperscissors.ports.inbound.http.api.v1.dto.OutboundStartGameDTO;
import com.rocketpaperscissors.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.rocketpaperscissors.ports.inbound.http.api.v1.exception.NotFoundException;
import com.rocketpaperscissors.ports.outbound.database.game.dao.GameDAO;
import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import com.rocketpaperscissors.ports.outbound.observability.MetricCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/game")
@Valid
@Slf4j
public class GameControllerApiV1 extends BaseController {

    @Autowired
    private InboundGameAdapter inboundGameAdapter;

    @Autowired
    private MetricCollector metricCollector;

    @PostMapping("/start")
    public ResponseEntity<OutboundStartGameDTO> start() throws ForbiddenException {
        UserDAO user = this.authenticate();

        GameDAO gameDAO = this.outboundGameAdapter.createGameForUser(user);

        OutboundStartGameDTO outboundStartGameDTO = new OutboundStartGameDTO();
        outboundStartGameDTO.setGameId(gameDAO.getId());

        metricCollector.incrementMetric("game_started");
        log.info("Game started with id: {} for player {}", gameDAO.getId(), user.getId());

        return ResponseEntity.ok(outboundStartGameDTO);
    }

    @GetMapping("/{gameId}/next_server_move")
    public ResponseEntity<OutboundServerMoveDTO> nextServerMove(@PathVariable Long gameId) throws ForbiddenException, NotFoundException {
        UserDAO userDAO = this.verifyOwner(gameId);

        String hash = this.inboundGameAdapter.nextServerMove(gameId, userDAO.getId());

        OutboundServerMoveDTO outboundServerMoveDTO = new OutboundServerMoveDTO();
        outboundServerMoveDTO.setHash(hash);

        return ResponseEntity.ok(outboundServerMoveDTO);
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<OutboundGameResultDTO> move(@PathVariable Long gameId, @RequestBody InboundGameResultDTO inboundGameResultDTO) throws ForbiddenException, NotFoundException {
        UserDAO userDAO = this.verifyOwner(gameId);

        return ResponseEntity.ok(this.inboundGameAdapter.result(gameId, inboundGameResultDTO.getPlayerMovement().name(), userDAO.getId()));
    }

    @PostMapping("/{gameId}/end")
    public ResponseEntity<Void> end(@PathVariable Long gameId) throws ForbiddenException, NotFoundException {
        this.verifyOwner(gameId);

        this.outboundGameAdapter.endGame(gameId);

        metricCollector.incrementMetric("game_ended");
        log.info("Game ended with id: {}", gameId);

        return ResponseEntity.noContent().build();
    }
}
