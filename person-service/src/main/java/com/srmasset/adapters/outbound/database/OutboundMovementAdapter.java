package com.srmasset.adapters.outbound.database;

import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import com.srmasset.ports.outbound.database.movement.OutboundMovementRepositoryPort;
import com.srmasset.ports.outbound.database.movement.dao.MovementDAO;
import com.srmasset.ports.outbound.database.movement.dao.MovementValueDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboundMovementAdapter {

    @Autowired
    private OutboundMovementRepositoryPort outboundMovementRepositoryPort;

    public void saveMovement(MovementValueDAO movementValueDAO, String salt, String hash, Long gameId) {
        MovementDAO movementDAO = new MovementDAO();
        movementDAO.setValue(movementValueDAO);
        movementDAO.setSalt(salt);
        GameDAO gameDAO = new GameDAO();
        gameDAO.setId(gameId);
        movementDAO.setGame(gameDAO);
        movementDAO.setHash(hash);
        this.outboundMovementRepositoryPort.save(movementDAO);
    }
}
