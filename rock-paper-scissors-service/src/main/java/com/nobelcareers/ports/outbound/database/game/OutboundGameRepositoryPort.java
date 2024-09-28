package com.nobelcareers.ports.outbound.database.game;

import com.nobelcareers.ports.outbound.database.game.dao.GameDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundGameRepositoryPort extends JpaRepository<GameDAO, Long> {

    @Query("SELECT g.winner.id, COUNT(g) FROM GameDAO g WHERE g.owner.id = :ownerId AND g.statusDAO = 'CLOSED' GROUP BY g.winner.id")
    List<Object[]> findAllGamesOfAPlayerGroupedByWinner(Long ownerId);
}