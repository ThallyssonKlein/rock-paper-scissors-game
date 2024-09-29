package com.nobelcareers.adapters.inbound.http.api.v1;

import com.nobelcareers.adapters.outbound.database.OutboundResultAdapter;
import com.nobelcareers.domain.statistics.StatisticsBO;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundStatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class InboundStatisticsAdapterTest {

    @Mock
    private OutboundResultAdapter outboundResultAdapter;

    @InjectMocks
    private InboundStatisticsAdapter inboundStatisticsAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStatistics_withMultipleResults_returnsCorrectStatistics() {
        Long playerId = 1L;
        List<Object[]> results = List.of(
                new Object[]{"WIN", 5},
                new Object[]{"LOSE", 3},
                new Object[]{"DRAW", 2}
        );
        when(outboundResultAdapter.findAllResultsOfAPlayer(playerId)).thenReturn(results);

        OutboundStatisticsDTO dto = inboundStatisticsAdapter.getStatistics(playerId);

        assertEquals(10, dto.getCountOfGames());
        assertEquals(5, dto.getCountOfVictories());
        assertEquals(3, dto.getCountOfDefeats());
        assertEquals(2, dto.getCountOfDraws());
    }

    @Test
    void getStatistics_withEmptyResults_returnsZeroStatistics() {
        Long playerId = 1L;
        List<Object[]> results = List.of();
        when(outboundResultAdapter.findAllResultsOfAPlayer(playerId)).thenReturn(results);

        OutboundStatisticsDTO dto = inboundStatisticsAdapter.getStatistics(playerId);

        assertEquals(0, dto.getCountOfGames());
        assertEquals(0, dto.getCountOfVictories());
        assertEquals(0, dto.getCountOfDefeats());
        assertEquals(0, dto.getCountOfDraws());
    }

    @Test
    void getStatistics_withNullResults_throwsException() {
        Long playerId = 1L;
        when(outboundResultAdapter.findAllResultsOfAPlayer(playerId)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> inboundStatisticsAdapter.getStatistics(playerId));
    }
}