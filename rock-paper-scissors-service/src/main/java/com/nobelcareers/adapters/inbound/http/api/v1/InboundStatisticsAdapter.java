package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InboundStatisticsAdapter {

    @Autowired
    private OutboundGameAdapter outboundGameAdapter;

    public OutboundStatisticsDTO getStatistics(Long playerId) {
        List<Object[]> results = outboundGameAdapter.findAllGamesOfAPlayerGroupedByWinner(playerId);

        int countOfGames = 0;
        int countOfVictories = 0;
        int countOfDefeats = 0;
        int countOfDraws;

        for (Object[] result : results) {
            Long winnerId = (Long) result[0];
            Long count = (Long) result[1];

            countOfGames += count.intValue();
            if (winnerId.equals(playerId)) {
                countOfVictories += count.intValue();
            } else {
                countOfDefeats += count.intValue();
            }
        }

        // Assuming draws are not included in the query results
        countOfDraws = countOfGames - (countOfVictories + countOfDefeats);

        return new OutboundStatisticsDTO(countOfGames, countOfVictories, countOfDefeats, countOfDraws);
    }
}
