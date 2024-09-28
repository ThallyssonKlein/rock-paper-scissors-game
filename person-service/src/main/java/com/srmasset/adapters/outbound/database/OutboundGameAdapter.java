package com.srmasset.adapters.outbound.database;

import com.srmasset.adapters.outbound.database.exception.GameNotFoundException;
import com.srmasset.ports.outbound.database.game.OutboundGameRepositoryPort;
import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import com.srmasset.ports.outbound.database.game.dao.StatusDAO;
import com.srmasset.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboundGameAdapter {

    @Autowired
    private OutboundGameRepositoryPort outboundGameRepositoryPort;

    public GameDAO findGameById(Long gameId) {
        return outboundGameRepositoryPort.findById(gameId).orElseThrow(GameNotFoundException::new);
    }

    public GameDAO createGameForUser(UserDAO userDAO) {
        GameDAO gameDAO = new GameDAO();
        gameDAO.setOwner(userDAO);
        return outboundGameRepositoryPort.save(gameDAO);
    }

    public StatusDAO getStatusByGameId(Long gameId) {
        return findGameById(gameId).getStatusDAO();
    }

    public void closeGame(Long gameId) {
        GameDAO gameDAO = findGameById(gameId);
        gameDAO.setStatusDAO(StatusDAO.CLOSED);
        outboundGameRepositoryPort.save(gameDAO);
    }
}
