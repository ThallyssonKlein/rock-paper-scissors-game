package com.srmasset.ports.outbound.database.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutboundUserRepositoryPort extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findByUsername(String username);
}
