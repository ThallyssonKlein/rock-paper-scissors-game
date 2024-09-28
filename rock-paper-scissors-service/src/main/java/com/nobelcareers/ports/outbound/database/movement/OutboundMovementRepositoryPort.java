package com.nobelcareers.ports.outbound.database.movement;

import com.nobelcareers.ports.outbound.database.movement.dao.MovementDAO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundMovementRepositoryPort extends JpaRepository<MovementDAO, Long> {
    @Query("SELECT m FROM MovementDAO m WHERE m.player.id = ?1")
    List<MovementDAO> findAllMovementsFromOnePlayer(Long playerId);

    @Query("SELECT m FROM MovementDAO m WHERE m.game.id = ?1 ORDER BY m.createdAt DESC")
    List<MovementDAO> findLastServerMovementByGameId(Long gameId, Pageable pageable);
}