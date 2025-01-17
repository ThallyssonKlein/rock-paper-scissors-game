package com.rocketpaperscissors.adapters.outbound.database;

import com.rocketpaperscissors.domain.game.bo.WinnerBO;
import com.rocketpaperscissors.ports.outbound.database.game.dao.GameDAO;
import com.rocketpaperscissors.ports.outbound.database.result.OutboundResultRepositoryPort;
import com.rocketpaperscissors.ports.outbound.database.result.ResultDAO;
import com.rocketpaperscissors.ports.outbound.database.result.ResultValueDAO;
import com.rocketpaperscissors.ports.outbound.database.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboundResultAdapter {

    @Autowired
    private OutboundResultRepositoryPort outboundResultRepositoryPort;

    @Value("${game.server_player_id}")
    private Long serverPlayerId;

    public void saveResults(WinnerBO result, Long gameId, Long playerId) {
        if (result == WinnerBO.PLAYER) {
            ResultDAO resultDAO = new ResultDAO();
            resultDAO.setGame(new GameDAO(gameId));
            resultDAO.setPlayer(new UserDAO(playerId));
            resultDAO.setResult(ResultValueDAO.WIN);
            outboundResultRepositoryPort.save(resultDAO);

            ResultDAO resultServerDAO = new ResultDAO();
            resultServerDAO.setGame(new GameDAO(gameId));
            resultServerDAO.setPlayer(new UserDAO(serverPlayerId));
            resultServerDAO.setResult(ResultValueDAO.LOSE);
            outboundResultRepositoryPort.save(resultServerDAO);
        } else if (result == WinnerBO.SERVER) {
            ResultDAO resultDAO = new ResultDAO();
            resultDAO.setGame(new GameDAO(gameId));
            resultDAO.setPlayer(new UserDAO(playerId));
            resultDAO.setResult(ResultValueDAO.LOSE);
            outboundResultRepositoryPort.save(resultDAO);

            ResultDAO resultPlayerDAO = new ResultDAO();
            resultPlayerDAO.setGame(new GameDAO(gameId));
            resultPlayerDAO.setPlayer(new UserDAO(serverPlayerId));
            resultPlayerDAO.setResult(ResultValueDAO.WIN);
            outboundResultRepositoryPort.save(resultPlayerDAO);
        } else {
            ResultDAO resultDAO = new ResultDAO();
            resultDAO.setGame(new GameDAO(gameId));
            resultDAO.setPlayer(new UserDAO(playerId));
            resultDAO.setResult(ResultValueDAO.DRAW);
            outboundResultRepositoryPort.save(resultDAO);

            ResultDAO resultServerDAO = new ResultDAO();
            resultServerDAO.setGame(new GameDAO(gameId));
            resultServerDAO.setPlayer(new UserDAO(serverPlayerId));
            resultServerDAO.setResult(ResultValueDAO.DRAW);
            outboundResultRepositoryPort.save(resultServerDAO);
        }
    }

    public List<Object[]> findAllResultsOfAPlayer(Long playerId) {
        return outboundResultRepositoryPort.findAllResultsOfAPlayer(playerId);
    }
}
