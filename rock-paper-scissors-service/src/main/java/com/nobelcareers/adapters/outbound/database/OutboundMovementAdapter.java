package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.domain.game.bo.MovementBO;
import com.nobelcareers.domain.game.bo.MovementValueBO;
import com.nobelcareers.ports.outbound.database.game.dao.GameDAO;
import com.nobelcareers.ports.outbound.database.movement.OutboundMovementRepositoryPort;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementDAO;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementValueDAO;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
        return movementDAOList.stream().map(this::mapMovementDAOToMovementBO).toList();
    }

    public MovementDAO getLastServerMovementByGameId(Long gameId) {
        List<MovementDAO> movements = this.outboundMovementRepositoryPort.findLastServerMovementByGameId(gameId, PageRequest.of(0, 1));
        return movements.isEmpty() ? null : movements.get(0);
    }
}