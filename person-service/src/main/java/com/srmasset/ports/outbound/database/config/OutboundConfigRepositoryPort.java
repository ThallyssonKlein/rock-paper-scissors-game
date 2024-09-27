package com.srmasset.ports.outbound.database.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboundConfigRepositoryPort extends JpaRepository<ConfigDAO, Long>{
}
