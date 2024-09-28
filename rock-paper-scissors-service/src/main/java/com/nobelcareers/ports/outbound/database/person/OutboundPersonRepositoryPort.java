package com.nobelcareers.ports.outbound.database.person;

import com.nobelcareers.ports.outbound.database.person.dao.PersonDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboundPersonRepositoryPort extends JpaRepository<PersonDAO, Long> {
}
