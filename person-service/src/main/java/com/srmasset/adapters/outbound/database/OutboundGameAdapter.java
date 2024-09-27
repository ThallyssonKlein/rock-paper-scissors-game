package com.srmasset.adapters.outbound.database;

import com.srmasset.adapters.outbound.database.exception.GameNotFound;
import com.srmasset.ports.outbound.database.game.OutboundGameRepositoryPort;
import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutboundGameAdapter {

    @Autowired
    private OutboundGameRepositoryPort outboundGameRepositoryPort;

    // findGameById
    public GameDAO findGameById(Long gameId) {
        return outboundGameRepositoryPort.findById(gameId).orElseThrow(GameNotFound::new);
    }
}
