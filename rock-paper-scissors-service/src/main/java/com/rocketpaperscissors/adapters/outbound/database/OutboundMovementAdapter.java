package com.rocketpaperscissors.adapters.outbound.database;

import com.rocketpaperscissors.domain.game.bo.MovementBO;
import com.rocketpaperscissors.domain.game.bo.MovementValueBO;
import com.rocketpaperscissors.ports.outbound.database.game.dao.GameDAO;
import com.rocketpaperscissors.ports.outbound.database.movement.OutboundMovementRepositoryPort;
import com.rocketpaperscissors.ports.outbound.database.movement.dao.MovementDAO;
import com.rocketpaperscissors.ports.outbound.database.movement.dao.MovementValueDAO;
import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OutboundMovementAdapter {

    @Autowired
    private OutboundMovementRepositoryPort outboundMovementRepositoryPort;

    public void saveMovement(MovementValueDAO movementValueDAO, String salt, String hash, Long gameId, Long playerId) {
        MovementDAO movementDAO = new MovementDAO();
        movementDAO.setValue(movementValueDAO);
        movementDAO.setSalt(salt);
        GameDAO gameDAO = new GameDAO();
        gameDAO.setId(gameId);
        movementDAO.setGame(gameDAO);
        movementDAO.setHash(hash);
        UserDAO userDAO = new UserDAO();
        userDAO.setId(playerId);
        movementDAO.setPlayer(userDAO);
        this.outboundMovementRepositoryPort.save(movementDAO);
    }

    private MovementBO mapMovementDAOToMovementBO(MovementDAO movementDAO) {
        MovementBO movementBO = new MovementBO();
        movementBO.setValue(MovementValueBO.valueOf(movementDAO.getValue().name()));
        return movementBO;
    }

    public List<MovementBO> findAllMovementsFromOnePlayer(Long playerId) {
        List<MovementDAO> movementDAOList = this.outboundMovementRepositoryPort.findAllMovementsFromOnePlayer(playerId);
        log.info("Movements found for player with id: {}", playerId);
        List<MovementBO> movementBOS = movementDAOList.stream().map(this::mapMovementDAOToMovementBO).toList();
        log.info("Movements mapped for player with id: {}", playerId);
        return movementBOS;
    }

    public MovementDAO getLastServerMovementByGameId(Long gameId) {
        List<MovementDAO> movements = this.outboundMovementRepositoryPort.findLastServerMovementByGameId(gameId, PageRequest.of(0, 1));
        return movements.isEmpty() ? null : movements.get(0);
    }
}