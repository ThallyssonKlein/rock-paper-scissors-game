package com.srmasset.adapters.inbound.http;

import com.srmasset.adapters.outbound.database.OutboundGameAdapter;
import com.srmasset.adapters.outbound.database.OutboundMovementAdapter;
import com.srmasset.adapters.outbound.database.exception.GameClosedException;
import com.srmasset.domain.game.GameService;
import com.srmasset.domain.game.MovementValueBO;
import com.srmasset.domain.game.WinnerBO;
import com.srmasset.ports.inbound.http.api.v1.dto.OutboundGameResultDTO;
import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import com.srmasset.ports.outbound.database.game.dao.StatusDAO;
import com.srmasset.ports.outbound.database.movement.dao.MovementDAO;
import com.srmasset.ports.outbound.database.movement.dao.MovementValueDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InboundGameAdapter {
    @Autowired
    private GameService gameService;

    @Autowired
    private OutboundMovementAdapter outboundMovementAdapter;

    @Autowired
    private OutboundGameAdapter outboundGameAdapter;

    @Value("${game.server_player_id}")
    private Long serverPlayerId;

    public String nextServerMove(Long gameId, Long playerId) {
        StatusDAO status = this.outboundGameAdapter.getStatusByGameId(gameId);

        if (status != StatusDAO.OPENED) {
            throw new GameClosedException();
        }

        String serverMovement = this.gameService.generateServerMovement(playerId);
        String salt = this.gameService.generateSalt();
        String hash = this.gameService.generateHash(serverMovement, salt);

        this.outboundMovementAdapter.saveMovement(MovementValueDAO.valueOf(serverMovement), salt, hash, gameId, serverPlayerId);

        return hash;
    }

    public OutboundGameResultDTO result(Long gameId, String playerMove) {
        MovementDAO lastServerMovement = this.outboundMovementAdapter.getLastServerMovementByGameId(gameId);

        WinnerBO result = this.gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf(lastServerMovement.getValue().name()));

        OutboundGameResultDTO outboundGameResultDTO = new OutboundGameResultDTO();
        outboundGameResultDTO.setHash(lastServerMovement.getHash());
        outboundGameResultDTO.setSalt(lastServerMovement.getSalt());
        outboundGameResultDTO.setServerMove(lastServerMovement.getValue().name());
        outboundGameResultDTO.setResult(result.name());

        return outboundGameResultDTO;
    }
}
