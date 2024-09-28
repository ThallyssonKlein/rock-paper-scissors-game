package com.nobelcareers.domain.statistics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {
    public StatisticsBO calculateStatistics(List<Object[]> results, Long playerId) {
        int countOfGames = 0;
        int countOfVictories = 0;
        int countOfDefeats = 0;
        int countOfDraws = 0;

        for (Object[] result : results) {
            Long winnerId = (Long) result[0];
            Long count = (Long) result[1];

            countOfGames += count.intValue();

            if(winnerId == null) {
                countOfDraws = count.intValue();
                continue;
            }

            if (winnerId.equals(playerId)) {
                countOfVictories += count.intValue();
            } else {
                countOfDefeats += count.intValue();
            }
        }

        return new StatisticsBO(countOfGames, countOfVictories, countOfDefeats, countOfDraws);
    }
}
