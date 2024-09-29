package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundResultAdapter;
import com.nobelcareers.domain.statistics.ResultValueBO;
import com.nobelcareers.domain.statistics.StatisticsBO;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import com.nobelcareers.ports.outbound.database.result.ResultDAO;
import com.nobelcareers.ports.outbound.database.result.ResultValueDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InboundStatisticsAdapter {

    @Autowired
    private OutboundResultAdapter outboundResultAdapter;

    public OutboundStatisticsDTO getStatistics(Long playerId) {
        List<Object[]> results = this.outboundResultAdapter.findAllResultsOfAPlayer(playerId);
        log.info("Results found: {}", results.size());

        StatisticsBO statisticsBO = new StatisticsBO();
        for (Object[] result : results) {
            ResultValueBO resultType = ResultValueBO.valueOf(((ResultValueDAO) result[0]).name());
            Integer count = ((Number) result[1]).intValue();
            statisticsBO.addResult(resultType, count);
        }
        log.info("Statistics calculated: {}", statisticsBO);

        if (results.isEmpty()) {
            return new OutboundStatisticsDTO(0, 0, 0, 0);
        }

        return new OutboundStatisticsDTO(statisticsBO.getCountOfGames(), statisticsBO.getCountOfVictories(), statisticsBO.getCountOfDefeats(), statisticsBO.getCountOfDraws());
    }
}
