package com.nobelcareers.ports.outbound.database.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundResultRepositoryPort extends JpaRepository<ResultDAO, Long> {
    @Query("SELECT r.result, COUNT(r.result) FROM ResultDAO r WHERE r.player.id = ?1 GROUP BY r.result")
    List<Object[]> findAllResultsOfAPlayer(Long playerId);
}
