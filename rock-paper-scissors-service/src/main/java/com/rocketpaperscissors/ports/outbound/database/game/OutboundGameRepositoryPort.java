package com.rocketpaperscissors.ports.outbound.database.game;

import com.rocketpaperscissors.ports.outbound.database.game.dao.GameDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboundGameRepositoryPort extends JpaRepository<GameDAO, Long> {
}