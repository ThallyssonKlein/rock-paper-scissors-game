package com.srmasset.ports.outbound.database.movement;

import com.srmasset.ports.outbound.database.movement.dao.MovementDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundMovementRepositoryPort extends JpaRepository<MovementDAO, Long> {
    // findAllMovementsFromOnePlayer
    @Query("SELECT m FROM MovementDAO m WHERE AND m.player.id = ?1")
    List<MovementDAO> findAllMovementsFromOnePlayer(Long playerId);
}
