package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundMovementAdapter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

    public String nextServerMove(Long gameId, Long playerId) throws ForbiddenException, NotFoundException {
        StatusDAO status = this.outboundGameAdapter.getStatusByGameId(gameId);

        if (status != StatusDAO.OPENED) {
            throw new ForbiddenException("Game closed");
        }

        List<MovementBO> movementsFromPlayer = this.outboundMovementAdapter.findAllMovementsFromOnePlayer(playerId);

        String serverMovement = this.movementService.generateServerMovement(playerId, movementsFromPlayer);
        String salt = this.movementService.generateSalt();
        String hash = this.movementService.generateHash(serverMovement, salt);

        this.outboundMovementAdapter.saveMovement(MovementValueDAO.valueOf(serverMovement), salt, hash, gameId, serverPlayerId);

        return hash;
    }

    public OutboundGameResultDTO result(Long gameId, String playerMove, Long playerId) throws ForbiddenException, NotFoundException {
        StatusDAO status = this.outboundGameAdapter.getStatusByGameId(gameId);

        if (status != StatusDAO.OPENED) {
            throw new ForbiddenException("Game closed");
        }

        MovementDAO lastServerMovement = this.outboundMovementAdapter.getLastServerMovementByGameId(gameId);

        WinnerBO result = this.gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf(lastServerMovement.getValue().name()));

        OutboundGameResultDTO outboundGameResultDTO = new OutboundGameResultDTO();
        outboundGameResultDTO.setHash(lastServerMovement.getHash());
        outboundGameResultDTO.setSalt(lastServerMovement.getSalt());
        outboundGameResultDTO.setServerMove(lastServerMovement.getValue().name());
        outboundGameResultDTO.setResult(result.name());

        switch(result) {
            case PLAYER:
                this.outboundGameAdapter.defineGameWinner(gameId, playerId);
                break;
            case SERVER:
                this.outboundGameAdapter.defineGameWinner(gameId, serverPlayerId);
                break;
            case DRAW:
                this.outboundGameAdapter.closeGame(gameId);
                break;
        }

        return outboundGameResultDTO;
    }
}
