package com.srmasset.ports.outbound.database.game;

import com.srmasset.ports.outbound.database.game.dao.GameDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundGameRepositoryPort extends JpaRepository<GameDAO, Long> {

    @Query("SELECT g FROM GameDAO g WHERE g.owner.id = :ownerId and g.statusDAO = 'CLOSED' GROUPED BY g.winner.id")
    List<GameDAO> findAllGamesOfAPlayerGroupedByWinner(Long ownerId);
}

