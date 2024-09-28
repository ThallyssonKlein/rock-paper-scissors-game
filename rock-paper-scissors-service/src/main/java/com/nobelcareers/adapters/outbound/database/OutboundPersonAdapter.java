package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.ports.inbound.http.api.v1.exception.NotFoundException;
import com.nobelcareers.ports.outbound.database.IdentifierType;
import com.nobelcareers.domain.person.service.PersonService;
import com.nobelcareers.ports.inbound.http.api.v1.dto.InboundPersonDTO;
import com.nobelcareers.ports.outbound.database.person.OutboundPersonRepositoryPort;
import com.nobelcareers.ports.outbound.database.person.dao.PersonDAO;
import com.nobelcareers.ports.inbound.http.api.v1.exception.InternalErrorException;
import com.nobelcareers.ports.outbound.observability.MetricCollector;
import datadog.trace.api.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OutboundPersonAdapter {

    @Autowired
    private OutboundPersonRepositoryPort personRepositoryPort;

    @Autowired
    private PersonService personService;

    @Autowired
    private MetricCollector metricCollector;

    @Trace(operationName = "OutboundPersonAdapter.convertInboundPersonDtoToPersonDAO")
    private PersonDAO convertInboundPersonDtoToPersonDAO(InboundPersonDTO person) {
        PersonDAO personDAO = new PersonDAO();
        personDAO.setName(person.getName());
        personDAO.setIdentifier(person.getIdentifier());
        personDAO.setBirthDate(person.getBirthDate());

        IdentifierType identifierType = this.personService.identifyIdentifierType(person.getIdentifier());
        personDAO.setIdentifierType(identifierType);
        personDAO.setMinMonthlyValue(this.personService.generateMinMonthlyValue(identifierType));
        personDAO.setMaxLoanValue(this.personService.generateMaxLoanValue(identifierType));

        return personDAO;
    }

    @Trace(operationName = "OutboundPersonAdapter.createPerson")
    public PersonDAO createPerson(InboundPersonDTO inboundPersonDTO) throws InternalErrorException {
        try {
            PersonDAO personDAO = this.personRepositoryPort.save(convertInboundPersonDtoToPersonDAO(inboundPersonDTO));
            this.metricCollector.incrementMetric("person_created");

            return personDAO;
        } catch (Exception e) {
            log.error(e.getMessage());
            this.metricCollector.incrementMetric("unexpected_error_create_person");
            throw new InternalErrorException("Unexpected error creating person");
        }
    }

    @Trace(operationName = "OutboundPersonAdapter.getPerson")
    public PersonDAO getPerson(Long id) throws InternalErrorException, NotFoundException {
        try {
            return this.personRepositoryPort.findById(id).orElseThrow(() -> new NotFoundException("Person not found!"));
        } catch (NotFoundException e) {
            this.metricCollector.incrementMetric("person_not_found");
            throw e;
        } catch (Exception e) {
            this.metricCollector.incrementMetric("unexpected_error_get_person");
            throw new InternalErrorException("Unexpected error getting person");
        }
    }
}
