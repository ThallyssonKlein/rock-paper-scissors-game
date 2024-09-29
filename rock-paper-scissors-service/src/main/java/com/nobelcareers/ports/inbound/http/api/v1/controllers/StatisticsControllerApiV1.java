package com.nobelcareers.ports.inbound.http.api.v1.controllers;

import com.nobelcareers.adapters.inbound.http.api.v1.InboundStatisticsAdapter;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import com.nobelcareers.ports.outbound.observability.MetricCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsControllerApiV1 extends BaseController {

    @Autowired
    private InboundStatisticsAdapter inboundStatisticsAdapter;

    @Autowired
    private MetricCollector metricCollector;

    @GetMapping("/{userId}")
    public OutboundStatisticsDTO getStatistics(@PathVariable Long userId) throws ForbiddenException {
        UserDAO userDAO = this.authenticate();

        if (!userId.equals(userDAO.getId())) {
            throw new ForbiddenException();
        }

        this.metricCollector.incrementMetric("statistics.get");

        return this.inboundStatisticsAdapter.getStatistics(userId);
    }
}
