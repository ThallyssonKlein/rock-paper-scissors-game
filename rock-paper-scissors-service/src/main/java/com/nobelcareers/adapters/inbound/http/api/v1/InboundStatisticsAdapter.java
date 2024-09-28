package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.domain.statistics.StatisticsBO;
import com.nobelcareers.domain.statistics.StatisticsService;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InboundStatisticsAdapter {

    @Autowired
    private OutboundGameAdapter outboundGameAdapter;

    @Autowired
    private StatisticsService statisticsService;

    public OutboundStatisticsDTO getStatistics(Long playerId) {
        List<Object[]> results = this.outboundGameAdapter.findAllGamesOfAPlayerGroupedByWinner(playerId);

        StatisticsBO statisticsBO = this.statisticsService.calculateStatistics(results, playerId);

        return new OutboundStatisticsDTO(statisticsBO.getCountOfGames(), statisticsBO.getCountOfVictories(), statisticsBO.getCountOfDefeats(), statisticsBO.getCountOfDraws());
    }
}
