package com.rocketpaperscissors.adapters.inbound.http.api.v1;

import com.rocketpaperscissors.adapters.outbound.cache.OutboundRedisAdapter;
import com.rocketpaperscissors.adapters.outbound.database.OutboundGameAdapter;
import com.rocketpaperscissors.adapters.outbound.database.OutboundMovementAdapter;
import com.rocketpaperscissors.adapters.outbound.database.OutboundResultAdapter;
import com.rocketpaperscissors.domain.game.GameService;
import com.rocketpaperscissors.domain.game.bo.MovementBO;
import com.rocketpaperscissors.domain.game.bo.MovementValueBO;
import com.rocketpaperscissors.domain.game.bo.WinnerBO;
import com.rocketpaperscissors.domain.movement.MovementService;
import com.rocketpaperscissors.ports.inbound.http.api.v1.dto.OutboundGameResultDTO;
import com.rocketpaperscissors.ports.inbound.http.api.v1.exception.ForbiddenException;
import com.rocketpaperscissors.ports.inbound.http.api.v1.exception.NotFoundException;
import com.rocketpaperscissors.ports.outbound.database.game.dao.StatusDAO;
import com.rocketpaperscissors.ports.outbound.database.movement.dao.MovementDAO;
import com.rocketpaperscissors.ports.outbound.database.movement.dao.MovementValueDAO;
import com.rocketpaperscissors.ports.outbound.observability.MetricCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InboundGameAdapterTest {

    @Mock
    private GameService gameService;

    @Mock
    private MovementService movementService;

    @Mock
    private OutboundMovementAdapter outboundMovementAdapter;

    @Mock
    private OutboundGameAdapter outboundGameAdapter;

    @Mock
    private MetricCollector metricCollector;

    @Mock
    private OutboundResultAdapter outboundResultAdapter;

    @Mock
    private OutboundRedisAdapter outboundRedisAdapter;

    @InjectMocks
    private InboundGameAdapter inboundGameAdapter;

    private final Long serverPlayerId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            Field serverPlayerIdField = InboundGameAdapter.class.getDeclaredField("serverPlayerId");
            serverPlayerIdField.setAccessible(true);
            serverPlayerIdField.set(inboundGameAdapter, serverPlayerId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testNextServerMove_PlayerTurn_ThrowsForbiddenException() throws NotFoundException {
        Long gameId = 1L;
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        when(outboundRedisAdapter.get(gameId.toString())).thenReturn(playerId.toString());

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> inboundGameAdapter.nextServerMove(gameId, playerId));
        assertEquals("It is your turn", exception.getMessage());
        verify(metricCollector, times(1)).incrementMetric("try_to_get_next_server_move_from_his_turn");
    }

    @Test
    void testNextServerMove_ClosedGame_ThrowsForbiddenException() throws NotFoundException {
        Long gameId = 1L;
        Long playerId = 2L;

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.CLOSED);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> inboundGameAdapter.nextServerMove(gameId, playerId));
        assertEquals("Game closed", exception.getMessage());
        verify(metricCollector, times(1)).incrementMetric("try_to_get_next_server_move_from_closed_game");
    }

    @Test
    void testNextServerMove_Successful() throws ForbiddenException, NotFoundException {
        Long gameId = 1L;
        Long playerId = 2L;
        List<MovementBO> movementsFromPlayer = Arrays.asList(new MovementBO());

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        when(outboundRedisAdapter.get(gameId.toString())).thenReturn(null); // Indica que é a vez do servidor
        when(outboundMovementAdapter.findAllMovementsFromOnePlayer(playerId)).thenReturn(movementsFromPlayer);
        when(movementService.generateServerMovement(movementsFromPlayer)).thenReturn("ROCK");
        when(movementService.generateSalt()).thenReturn("randomSalt");
        when(movementService.generateHash("ROCK", "randomSalt")).thenReturn("hashedValue");

        String result = inboundGameAdapter.nextServerMove(gameId, playerId);

        assertEquals("hashedValue", result);
        verify(outboundMovementAdapter, times(1)).saveMovement(MovementValueDAO.valueOf("ROCK"), "randomSalt", "hashedValue", gameId, serverPlayerId);
        verify(outboundRedisAdapter, times(1)).save(gameId.toString(), playerId.toString());
    }

    @Test
    void testResult_NotHisTurn_ThrowsForbiddenException() throws NotFoundException {
        Long gameId = 1L;
        Long playerId = 2L;
        String playerMove = "ROCK";

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        when(outboundRedisAdapter.get(gameId.toString())).thenReturn("3"); // ID diferente do jogador atual

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> inboundGameAdapter.result(gameId, playerMove, playerId));
        assertEquals("Not your turn", exception.getMessage());
        verify(metricCollector, times(1)).incrementMetric("try_to_get_result_from_not_his_turn");
    }

    @Test
    void testResult_ClosedGame_ThrowsForbiddenException() throws NotFoundException {
        Long gameId = 1L;
        Long playerId = 2L;
        String playerMove = "ROCK";

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.CLOSED);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> inboundGameAdapter.result(gameId, playerMove, playerId));
        assertEquals("Game closed", exception.getMessage());
        verify(metricCollector, times(1)).incrementMetric("try_to_get_result_from_closed_game");
    }

    @Test
    void testResult_Successful() throws ForbiddenException, NotFoundException {
        Long gameId = 1L;
        Long playerId = 2L;
        String playerMove = "ROCK";

        MovementDAO serverMovementDAO = new MovementDAO();
        serverMovementDAO.setValue(MovementValueDAO.valueOf("PAPER"));
        serverMovementDAO.setHash("hashValue");
        serverMovementDAO.setSalt("randomSalt");

        when(outboundGameAdapter.getStatusByGameId(gameId)).thenReturn(StatusDAO.OPENED);
        when(outboundRedisAdapter.get(gameId.toString())).thenReturn(playerId.toString());
        when(outboundMovementAdapter.getLastServerMovementByGameId(gameId)).thenReturn(serverMovementDAO);
        when(gameService.getWinner(MovementValueBO.valueOf(playerMove), MovementValueBO.valueOf("PAPER"))).thenReturn(WinnerBO.SERVER);

        OutboundGameResultDTO resultDTO = inboundGameAdapter.result(gameId, playerMove, playerId);

        assertEquals("PAPER", resultDTO.getServerMove());
        assertEquals("hashValue", resultDTO.getHash());
        assertEquals("randomSalt", resultDTO.getSalt());
        assertEquals("SERVER", resultDTO.getResult());

        verify(outboundResultAdapter, times(1)).saveResults(WinnerBO.SERVER, gameId, playerId);
        verify(outboundRedisAdapter, times(1)).save(gameId.toString(), serverPlayerId.toString());
    }
}
