package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class InboundStatisticsAdapterTest {

    @Mock
    private OutboundGameAdapter outboundGameAdapter;

    @InjectMocks
    private InboundStatisticsAdapter inboundStatisticsAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStatistics_ReturnsCorrectStatistics_WhenPlayerHasGames() {
        Long playerId = 1L;
        List<Object[]> results = Arrays.asList(
                new Object[]{1L, 3L},
                new Object[]{2L, 1L},
                new Object[]{null, 1L}
        );

        when(outboundGameAdapter.findAllGamesOfAPlayerGroupedByWinner(playerId)).thenReturn(results);

        OutboundStatisticsDTO statistics = inboundStatisticsAdapter.getStatistics(playerId);

        assertEquals(5, statistics.getCountOfGames());
        assertEquals(3, statistics.getCountOfVictories());
        assertEquals(1, statistics.getCountOfDefeats());
        assertEquals(1, statistics.getCountOfDraws());
    }

    @Test
    void getStatistics_ReturnsZeroStatistics_WhenPlayerHasNoGames() {
        Long playerId = 1L;
        List<Object[]> results = Collections.emptyList();

        when(outboundGameAdapter.findAllGamesOfAPlayerGroupedByWinner(playerId)).thenReturn(results);

        OutboundStatisticsDTO statistics = inboundStatisticsAdapter.getStatistics(playerId);

        assertEquals(0, statistics.getCountOfGames());
        assertEquals(0, statistics.getCountOfVictories());
        assertEquals(0, statistics.getCountOfDefeats());
        assertEquals(0, statistics.getCountOfDraws());
    }
}