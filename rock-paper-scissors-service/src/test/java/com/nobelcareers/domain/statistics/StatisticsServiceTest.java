package com.nobelcareers.domain.statistics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Arrays;

public class StatisticsServiceTest {

    private final StatisticsService statisticsService = new StatisticsService();

    @Test
    void calculateStatistics_withVictoriesAndDefeatsAndDraws() {
        List<Object[]> results = Arrays.asList(
            new Object[]{1L, 3L},
            new Object[]{2L, 2L},
            new Object[]{null, 1L}
        );
        Long playerId = 1L;

        StatisticsBO statistics = statisticsService.calculateStatistics(results, playerId);

        assertEquals(6, statistics.getCountOfGames());
        assertEquals(3, statistics.getCountOfVictories());
        assertEquals(2, statistics.getCountOfDefeats());
        assertEquals(1, statistics.getCountOfDraws());
    }

    @Test
    void calculateStatistics_withNoGames() {
        List<Object[]> results = Arrays.asList();
        Long playerId = 1L;

        StatisticsBO statistics = statisticsService.calculateStatistics(results, playerId);

        assertEquals(0, statistics.getCountOfGames());
        assertEquals(0, statistics.getCountOfVictories());
        assertEquals(0, statistics.getCountOfDefeats());
        assertEquals(0, statistics.getCountOfDraws());
    }
}