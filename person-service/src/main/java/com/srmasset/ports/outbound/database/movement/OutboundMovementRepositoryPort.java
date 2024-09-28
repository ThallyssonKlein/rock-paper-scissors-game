package com.srmasset.ports.outbound.database.movement;

import com.srmasset.ports.outbound.database.movement.dao.MovementDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboundMovementRepositoryPort extends JpaRepository<MovementDAO, Long> {
}
