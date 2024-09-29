package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.cache.OutboundRedisAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundMovementAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundResultAdapter;
import com.nobelcareers.domain.game.GameService;
import com.nobelcareers.domain.game.bo.MovementBO;
import com.nobelcareers.domain.game.bo.MovementValueBO;
import com.nobelcareers.domain.game.bo.WinnerBO;
import com.nobelcareers.domain.movement.MovementService;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundGameResultDTO;
import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.inbound.http.api.v1.exception.NotFoundException;
import com.nobelcareers.ports.outbound.database.game.dao.StatusDAO;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementDAO;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementValueDAO;
import com.nobelcareers.ports.outbound.observability.MetricCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InboundGameAdapter {

    @Autowired
    private GameService gameService;

    @Autowired
    private MovementService movementService;

    @Autowired
    private OutboundMovementAdapter outboundMovementAdapter;

    @Autowired
    private OutboundGameAdapter outboundGameAdapter;

    @Value("${game.server_player_id}")
    private Long serverPlayerId;

    @Autowired
    private MetricCollector metricCollector;

    @Autowired
    private OutboundResultAdapter outboundResultAdapter;

    @Autowired
    private OutboundRedisAdapter outboundRedisAdapter;

    public String nextServerMove(Long gameId, Long playerId) throws ForbiddenException, NotFoundException {
        StatusDAO status = this.outboundGameAdapter.getStatusByGameId(gameId);

        if (status != StatusDAO.OPENED) {
            metricCollector.incrementMetric("try_to_get_next_server_move_from_closed_game");
            log.warn("User {} tried to get next server move from closed game with id: {}", playerId, gameId);
            throw new ForbiddenException("Game closed");
        }

        String turn = (String) this.outboundRedisAdapter.get(gameId.toString());
        if (turn != null && turn.equals(playerId.toString())) {
            metricCollector.incrementMetric("try_to_get_next_server_move_from_his_turn");
            log.warn("User {} tried to get next server move from his turn in game with id: {}", playerId, gameId);
            throw new ForbiddenException("It is your turn");
        }

        List<MovementBO> movementsFromPlayer = this.outboundMovementAdapter.findAllMovementsFromOnePlayer(playerId);

        String serverMovement = this.movementService.generateServerMovement(movementsFromPlayer);
        log.info("Server movement generated for player with id: {}", playerId);
        String salt = this.movementService.generateSalt();
        log.info("Salt generated for player with id: {}", playerId);
        String hash = this.movementService.generateHash(serverMovement, salt);
        log.info("Hash generated for player with id: {}", playerId);

        this.outboundMovementAdapter.saveMovement(MovementValueDAO.valueOf(serverMovement), salt, hash, gameId, serverPlayerId);
        log.info("Server movement saved for player with id: {}", playerId);

        this.outboundRedisAdapter.save(gameId.toString(), playerId.toString());
        log.info("Turn changed to the player with id: {} for game with id: {}", playerId, gameId);

        return hash;
    }

    public OutboundGameResultDTO result(Long gameId, String playerMove, Long playerId) throws ForbiddenException, NotFoundException {
        StatusDAO status = this.outboundGameAdapter.getStatusByGameId(gameId);

        if (status != StatusDAO.OPENED) {
            metricCollector.incrementMetric("try_to_get_result_from_closed_game");
            log.warn("User {} tried to get result from closed game with id: {}", playerId, gameId);
            throw new ForbiddenException("Game closed");
        }

        String theTurn = (String) this.outboundRedisAdapter.get(gameId.toString());
        if (theTurn == null || !theTurn.equals(playerId.toString())) {
            metricCollector.incrementMetric("try_to_get_result_from_not_his_turn");
            log.warn("User {} tried to get result from game with id: {} but it is not his turn", playerId, gameId);
            throw new ForbiddenException("Not your turn");
        }

        MovementDAO lastServerMovement = this.outboundMovementAdapter.getLastServerMovementByGameId(gameId);
        log.info("Last server movement found for player with id: {}", playerId);

        WinnerBO result = this.gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf(lastServerMovement.getValue().name()));
        log.info("Result calculated for player with id: {}", playerId);

        OutboundGameResultDTO outboundGameResultDTO = new OutboundGameResultDTO();
        outboundGameResultDTO.setHash(lastServerMovement.getHash());
        outboundGameResultDTO.setSalt(lastServerMovement.getSalt());
        outboundGameResultDTO.setServerMove(lastServerMovement.getValue().name());
        outboundGameResultDTO.setResult(result.name());

        this.outboundResultAdapter.saveResults(result, gameId, playerId);
        log.info("Result saved for player with id: {}", playerId);

        this.outboundRedisAdapter.save(gameId.toString(), serverPlayerId.toString());
        log.info("Turn changed to the server for game with id: {}", gameId);

        return outboundGameResultDTO;
    }
}
