package com.rocketpaperscissors.adapters.outbound.database;

import com.rocketpaperscissors.domain.game.bo.WinnerBO;
import com.rocketpaperscissors.ports.outbound.database.result.OutboundResultRepositoryPort;
import com.rocketpaperscissors.ports.outbound.database.result.ResultDAO;
import com.rocketpaperscissors.ports.outbound.database.result.ResultValueDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OutboundResultAdapterTest {

    @Mock
    private OutboundResultRepositoryPort outboundResultRepositoryPort;

    @InjectMocks
    private OutboundResultAdapter outboundResultAdapter;

    @Captor
    private ArgumentCaptor<ResultDAO> resultCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(outboundResultAdapter, "serverPlayerId", 999L);
    }

    @Test
    void saveResults_PlayerWins() {
        outboundResultAdapter.saveResults(WinnerBO.PLAYER, 1L, 2L);

        verify(outboundResultRepositoryPort, times(2)).save(resultCaptor.capture());
        List<ResultDAO> results = resultCaptor.getAllValues();

        ResultDAO result = results.get(0);
        assertEquals(ResultValueDAO.WIN, result.getResult());
        assertEquals(2L, result.getPlayer().getId());

        ResultDAO resultServer = results.get(1);
        assertEquals(ResultValueDAO.LOSE, resultServer.getResult());
        assertEquals(999L, resultServer.getPlayer().getId());
    }

    @Test
    void saveResults_ServerWins() {
        outboundResultAdapter.saveResults(WinnerBO.SERVER, 1L, 2L);

        verify(outboundResultRepositoryPort, times(2)).save(resultCaptor.capture());
        List<ResultDAO> results = resultCaptor.getAllValues();

        ResultDAO result = results.get(0);
        assertEquals(ResultValueDAO.LOSE, result.getResult());
        assertEquals(2L, result.getPlayer().getId());

        ResultDAO resultServer = results.get(1);
        assertEquals(ResultValueDAO.WIN, resultServer.getResult());
        assertEquals(999L, resultServer.getPlayer().getId());
    }

    @Test
    void saveResults_Draw() {
        outboundResultAdapter.saveResults(WinnerBO.DRAW, 1L, 2L);

        verify(outboundResultRepositoryPort, times(2)).save(resultCaptor.capture());
        List<ResultDAO> results = resultCaptor.getAllValues();

        ResultDAO result = results.get(0);
        assertEquals(ResultValueDAO.DRAW, result.getResult());
        assertEquals(2L, result.getPlayer().getId());

        ResultDAO resultServer = results.get(1);
        assertEquals(ResultValueDAO.DRAW, resultServer.getResult());
        assertEquals(999L, resultServer.getPlayer().getId());
    }
}