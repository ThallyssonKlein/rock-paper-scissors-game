package com.srmasset.ports.inbound.http.api.v1.controllers;

import com.srmasset.adapters.inbound.http.InboundStatisticsAdapter;
import com.srmasset.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import com.srmasset.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.srmasset.ports.outbound.database.user.UserDAO;
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

    @GetMapping("/{userId}")
    public OutboundStatisticsDTO getStatistics(@PathVariable Long userId) throws ForbiddenException {
        UserDAO userDAO = this.authenticate();

        if (!userId.equals(userDAO.getId())) {
            throw new ForbiddenException();
        }

        return this.inboundStatisticsAdapter.getStatistics(userId);
    }
}
