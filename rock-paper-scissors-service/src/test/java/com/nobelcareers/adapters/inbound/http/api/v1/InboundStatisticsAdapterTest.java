package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.domain.statistics.StatisticsBO;
import com.nobelcareers.domain.statistics.StatisticsService;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InboundStatisticsAdapterTest {

    @Mock
    private OutboundGameAdapter outboundGameAdapter;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private InboundStatisticsAdapter inboundStatisticsAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStatistics_returnsCorrectStatistics() {
        Long playerId = 1L;
        StatisticsBO statisticsBO = new StatisticsBO(10, 5, 3, 2);

        when(statisticsService.calculateStatistics(any(), any())).thenReturn(statisticsBO);

        OutboundStatisticsDTO dto = inboundStatisticsAdapter.getStatistics(playerId);

        assertEquals(10, dto.getCountOfGames());
        assertEquals(5, dto.getCountOfVictories());
        assertEquals(3, dto.getCountOfDefeats());
        assertEquals(2, dto.getCountOfDraws());
    }

    @Test
    void getStatistics_withNoGames_returnsZeroStatistics() {
        Long playerId = 1L;
        StatisticsBO statisticsBO = new StatisticsBO(0, 0, 0, 0);

        when(statisticsService.calculateStatistics(any(), any())).thenReturn(statisticsBO);

        OutboundStatisticsDTO dto = inboundStatisticsAdapter.getStatistics(playerId);

        assertEquals(0, dto.getCountOfGames());
        assertEquals(0, dto.getCountOfVictories());
        assertEquals(0, dto.getCountOfDefeats());
        assertEquals(0, dto.getCountOfDraws());
    }
}