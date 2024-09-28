package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.domain.game.bo.MovementBO;
import com.nobelcareers.ports.outbound.database.movement.OutboundMovementRepositoryPort;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementDAO;
import com.nobelcareers.ports.outbound.database.movement.dao.MovementValueDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OutboundMovementAdapterTest {

    @Mock
    private OutboundMovementRepositoryPort outboundMovementRepositoryPort;

    @InjectMocks
    private OutboundMovementAdapter outboundMovementAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMovement_savesMovementSuccessfully() {
        MovementValueDAO movementValueDAO = MovementValueDAO.PAPER;
        String salt = "salt";
        String hash = "hash";
        Long gameId = 1L;
        Long playerId = 1L;

        outboundMovementAdapter.saveMovement(movementValueDAO, salt, hash, gameId, playerId);

        verify(outboundMovementRepositoryPort, times(1)).save(any(MovementDAO.class));
    }

    @Test
    void findAllMovementsFromOnePlayer_returnsMovementBOList() {
        Long playerId = 1L;
        MovementDAO movementDAO = new MovementDAO();
        MovementValueDAO movementValueDAO = MovementValueDAO.PAPER;
        movementDAO.setValue(movementValueDAO);
        when(outboundMovementRepositoryPort.findAllMovementsFromOnePlayer(playerId)).thenReturn(List.of(movementDAO));

        List<MovementBO> result = outboundMovementAdapter.findAllMovementsFromOnePlayer(playerId);

        assertEquals(1, result.size());
        verify(outboundMovementRepositoryPort, times(1)).findAllMovementsFromOnePlayer(playerId);
    }

    @Test
    void findAllMovementsFromOnePlayer_returnsEmptyListWhenNoMovements() {
        Long playerId = 1L;
        when(outboundMovementRepositoryPort.findAllMovementsFromOnePlayer(playerId)).thenReturn(Collections.emptyList());

        List<MovementBO> result = outboundMovementAdapter.findAllMovementsFromOnePlayer(playerId);

        assertEquals(0, result.size());
        verify(outboundMovementRepositoryPort, times(1)).findAllMovementsFromOnePlayer(playerId);
    }

    @Test
    void getLastServerMovementByGameId_returnsMovementDAO() {
        Long gameId = 1L;
        MovementDAO movementDAO = new MovementDAO();
        when(outboundMovementRepositoryPort.findLastServerMovementByGameId(gameId, PageRequest.of(0, 1))).thenReturn(List.of(movementDAO));

        MovementDAO result = outboundMovementAdapter.getLastServerMovementByGameId(gameId);

        assertEquals(movementDAO, result);
        verify(outboundMovementRepositoryPort, times(1)).findLastServerMovementByGameId(gameId, PageRequest.of(0, 1));
    }

    @Test
    void getLastServerMovementByGameId_returnsNullWhenNoMovement() {
        Long gameId = 1L;
        when(outboundMovementRepositoryPort.findLastServerMovementByGameId(gameId, PageRequest.of(0, 1))).thenReturn(Collections.emptyList());

        MovementDAO result = outboundMovementAdapter.getLastServerMovementByGameId(gameId);

        assertEquals(null, result);
        verify(outboundMovementRepositoryPort, times(1)).findLastServerMovementByGameId(gameId, PageRequest.of(0, 1));
    }
}