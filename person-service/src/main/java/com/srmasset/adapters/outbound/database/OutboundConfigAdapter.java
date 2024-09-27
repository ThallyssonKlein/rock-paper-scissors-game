package com.srmasset.adapters.outbound.database;

import com.srmasset.ports.outbound.database.config.ConfigDAO;
import com.srmasset.ports.outbound.database.config.OutboundConfigRepositoryPort;
import datadog.trace.api.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OutboundConfigAdapter {
    @Autowired
    private OutboundConfigRepositoryPort outboundConfigRepositoryPort;

    @Cacheable("configs")
    @Trace(operationName = "OutboundConfigAdapter.findAllConfigs")
    private List<ConfigDAO> findAllConfigs() {
        return this.outboundConfigRepositoryPort.findAll();
    }

    @Trace(operationName = "OutboundConfigAdapter.findPFMinMonthlyValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findPFMinMonthlyValue() {
        return this.findAllConfigs().stream().filter(config -> "PF".equals(config.getIdentifierType().name())).findFirst().get().getMinMonthlyValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findPFMaxLoanValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findPFMaxLoanValue() {
        return this.findAllConfigs().stream().filter(config -> "PF".equals(config.getIdentifierType().name())).findFirst().get().getMaxLoanValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findPJMinMonthlyValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findPJMinMonthlyValue() {
        return this.findAllConfigs().stream().filter(config -> "PJ".equals(config.getIdentifierType().name())).findFirst().get().getMinMonthlyValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findPJMaxLoanValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findPJMaxLoanValue() {
        return this.findAllConfigs().stream().filter(config -> "PJ".equals(config.getIdentifierType().name())).findFirst().get().getMaxLoanValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findEUMinMonthlyValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findEUMinMonthlyValue() {
        return this.findAllConfigs().stream().filter(config -> "EU".equals(config.getIdentifierType().name())).findFirst().get().getMinMonthlyValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findEUMaxLoanValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findEUMaxLoanValue() {
        return this.findAllConfigs().stream().filter(config -> "EU".equals(config.getIdentifierType().name())).findFirst().get().getMaxLoanValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findAPMinMonthlyValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findAPMinMonthlyValue() {
        return this.findAllConfigs().stream().filter(config -> "AP".equals(config.getIdentifierType().name())).findFirst().get().getMinMonthlyValue();
    }

    @Trace(operationName = "OutboundConfigAdapter.findAPMaxLoanValue")
    @SuppressWarnings("ConstantConditions")
    public BigDecimal findAPMaxLoanValue() {
        return this.findAllConfigs().stream().filter(config -> "AP".equals(config.getIdentifierType().name())).findFirst().get().getMaxLoanValue();
    }
}