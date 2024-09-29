package com.nobelcareers.adapters.inbound.http.api.v1;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nobelcareers.adapters.outbound.database.OutboundGameAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundMovementAdapter;
import com.nobelcareers.adapters.outbound.database.OutboundResultAdapter;
import com.nobelcareers.domain.game.GameService;
import com.nobelcareers.domain.game.bo.MovementValueBO;
import com.nobelcareers.domain.game.bo.WinnerBO;
import com.nobelcareers.ports.inbound.http.api.v1.dto.OutboundGameResultDTO;
import com.nobelcareers.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.nobelcareers.ports.inbound.http.api.v1.exception.NotFoundException;
import com.nobelcareers.ports.outbound.database.game.dao.StatusDAO;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementDAO;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementValueDAO;
import com.nobelcareers.ports.outbound.observability.MetricCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

class InboundGameAdapterTest {

    @Mock
    private GameService gameService;

    @Mock
    private OutboundMovementAdapter outboundMovementAdapter;

    @Mock
    private OutboundGameAdapter outboundGameAdapter;

    @Mock
    private MetricCollector metricCollector;

    @Mock
    private OutboundResultAdapter outboundResultAdapter;

    @InjectMocks
    private InboundGameAdapter inboundGameAdapter;

    @Value("${game.server_player_id}")
    private Long serverPlayerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void result_PlayerWins() throws ForbiddenException, NotFoundException {
        Long gameId = 1L;
        String playerMove = "ROCK";
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        MovementDAO lastServerMovement = new MovementDAO();
        lastServerMovement.setValue(MovementValueDAO.PAPER);
        when(outboundMovementAdapter.getLastServerMovementByGameId(gameId)).thenReturn(lastServerMovement);
        when(gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf(lastServerMovement.getValue().name())))
                .thenReturn(WinnerBO.PLAYER);

        OutboundGameResultDTO result = inboundGameAdapter.result(gameId, playerMove, playerId);

        assertEquals(WinnerBO.PLAYER.name(), result.getResult());
        verify(outboundResultAdapter, times(1)).saveResults(WinnerBO.PLAYER, gameId, playerId);
    }

    @Test
    void result_ServerWins() throws ForbiddenException, NotFoundException {
        Long gameId = 1L;
        String playerMove = "ROCK";
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        MovementDAO lastServerMovement = new MovementDAO();
        lastServerMovement.setValue(MovementValueDAO.SCISSORS);
        when(outboundMovementAdapter.getLastServerMovementByGameId(gameId)).thenReturn(lastServerMovement);
        when(gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf(lastServerMovement.getValue().name())))
                .thenReturn(WinnerBO.SERVER);

        OutboundGameResultDTO result = inboundGameAdapter.result(gameId, playerMove, playerId);

        assertEquals(WinnerBO.SERVER.name(), result.getResult());
        verify(outboundResultAdapter, times(1)).saveResults(WinnerBO.SERVER, gameId, playerId);
    }

    @Test
    void result_Draw() throws ForbiddenException, NotFoundException {
        Long gameId = 1L;
        String playerMove = "ROCK";
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        MovementDAO lastServerMovement = new MovementDAO();
        lastServerMovement.setValue(MovementValueDAO.ROCK);
        when(outboundMovementAdapter.getLastServerMovementByGameId(gameId)).thenReturn(lastServerMovement);
        when(gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf(lastServerMovement.getValue().name())))
                .thenReturn(WinnerBO.DRAW);

        OutboundGameResultDTO result = inboundGameAdapter.result(gameId, playerMove, playerId);

        assertEquals(WinnerBO.DRAW.name(), result.getResult());
        verify(outboundResultAdapter, times(1)).saveResults(WinnerBO.DRAW, gameId, playerId);
    }

    @Test
    void result_GameClosed() throws NotFoundException {
        Long gameId = 1L;
        String playerMove = "ROCK";
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.CLOSED);

        assertThrows(ForbiddenException.class, () -> inboundGameAdapter.result(gameId, playerMove, playerId));
    }

    @Test
    void result_ServerMovementNotFound() throws NotFoundException {
        Long gameId = 1L;
        String playerMove = "ROCK";
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        when(outboundMovementAdapter.getLastServerMovementByGameId(gameId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> inboundGameAdapter.result(gameId, playerMove, playerId));
    }
}