package com.nobelcareers.domain.person.service;

import com.nobelcareers.adapters.outbound.database.OutboundConfigAdapter;
import com.nobelcareers.ports.outbound.database.IdentifierType;
import com.nobelcareers.domain.person.error.InvalidIdentifierException;
import datadog.trace.api.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PersonService {

    @Autowired
    private OutboundConfigAdapter outboundConfigAdapter;

    @Trace(operationName = "PersonService.identifyIdentifierType")
    public IdentifierType identifyIdentifierType(String identifier) {
        return switch (identifier.length()) {
            case 11 -> IdentifierType.PF;
            case 14 -> IdentifierType.PJ;
            case 8 -> IdentifierType.EU;
            case 10 -> IdentifierType.AP;
            default -> throw new InvalidIdentifierException("Invalid identifier!");
        };
    }

    @Trace(operationName = "PersonService.generateMinMonthlyValue")
    public BigDecimal generateMinMonthlyValue(IdentifierType identifierType) {
        return switch (identifierType) {
            case PF -> this.outboundConfigAdapter.findPFMinMonthlyValue();
            case PJ -> this.outboundConfigAdapter.findPJMinMonthlyValue();
            case EU -> this.outboundConfigAdapter.findEUMinMonthlyValue();
            case AP -> this.outboundConfigAdapter.findAPMinMonthlyValue();
        };
    }

    @Trace(operationName = "PersonService.generateMaxLoanValue")
    public BigDecimal generateMaxLoanValue(IdentifierType identifierType) {
        return switch (identifierType) {
            case PF -> this.outboundConfigAdapter.findPFMaxLoanValue();
            case PJ -> this.outboundConfigAdapter.findPJMaxLoanValue();
            case EU -> this.outboundConfigAdapter.findEUMaxLoanValue();
            case AP -> this.outboundConfigAdapter.findAPMaxLoanValue();
        };
    }
}