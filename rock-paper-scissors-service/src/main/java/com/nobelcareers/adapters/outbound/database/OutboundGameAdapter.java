package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.ports.inbound.http.api.v1.exception.NotFoundException;
import com.nobelcareers.ports.outbound.database.game.OutboundGameRepositoryPort;
import com.nobelcareers.ports.outbound.database.game.dao.GameDAO;
import com.nobelcareers.ports.outbound.database.game.dao.StatusDAO;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OutboundGameAdapter {

    @Autowired
    private OutboundGameRepositoryPort outboundGameRepositoryPort;

    public GameDAO findGameById(Long gameId) throws NotFoundException {
        return outboundGameRepositoryPort.findById(gameId).orElseThrow(() -> new NotFoundException("Game not found"));
    }

    public GameDAO createGameForUser(UserDAO userDAO) {
        GameDAO gameDAO = new GameDAO();
        gameDAO.setOwner(userDAO);
        GameDAO gameDAO1 = outboundGameRepositoryPort.save(gameDAO);
        log.info("Game created with id: {} for player {}", gameDAO1.getId(), userDAO.getId());
        return gameDAO1;
    }

    public StatusDAO getStatusByGameId(Long gameId) throws NotFoundException {
        return this.findGameById(gameId).getStatusDAO();
    }

    public void closeGame(Long gameId) throws NotFoundException {
        GameDAO gameDAO = this.findGameById(gameId);
        gameDAO.setStatusDAO(StatusDAO.CLOSED);
        outboundGameRepositoryPort.save(gameDAO);
    }

    public void defineGameWinner(Long gameId, Long playerId) throws NotFoundException {
        GameDAO gameDAO = this.findGameById(gameId);
        UserDAO userDAO = new UserDAO();
        userDAO.setId(playerId);
        gameDAO.setWinner(userDAO);
        outboundGameRepositoryPort.save(gameDAO);
    }

    public List<Object[]> findAllGamesOfAPlayerGroupedByWinner(Long playerId) {
        return outboundGameRepositoryPort.findAllGamesOfAPlayerGroupedByWinner(playerId);
    }
}
