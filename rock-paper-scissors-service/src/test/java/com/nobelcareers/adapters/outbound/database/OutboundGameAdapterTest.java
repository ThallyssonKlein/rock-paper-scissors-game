package com.nobelcareers.adapters.outbound.database;

import com.nobelcareers.ports.inbound.http.api.v1.exception.NotFoundException;
import com.nobelcareers.ports.outbound.database.game.OutboundGameRepositoryPort;
import com.nobelcareers.ports.outbound.database.game.dao.GameDAO;
import com.nobelcareers.ports.outbound.database.game.dao.StatusDAO;
import com.nobelcareers.ports.outbound.database.user.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OutboundGameAdapterTest {

    @Mock
    private OutboundGameRepositoryPort outboundGameRepositoryPort;

    @InjectMocks
    private OutboundGameAdapter outboundGameAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findGameById_GameExists_ReturnsGame() throws NotFoundException {
        Long gameId = 1L;
        GameDAO gameDAO = new GameDAO();
        when(outboundGameRepositoryPort.findById(gameId)).thenReturn(Optional.of(gameDAO));

        GameDAO result = outboundGameAdapter.findGameById(gameId);

        assertEquals(gameDAO, result);
    }

    @Test
    void findGameById_GameDoesNotExist_ThrowsNotFoundException() {
        Long gameId = 1L;
        when(outboundGameRepositoryPort.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> outboundGameAdapter.findGameById(gameId));
    }

    @Test
    void createGameForUser_ValidUser_ReturnsCreatedGame() {
        UserDAO userDAO = new UserDAO();
        GameDAO gameDAO = new GameDAO();
        gameDAO.setOwner(userDAO);
        when(outboundGameRepositoryPort.save(any(GameDAO.class))).thenReturn(gameDAO);

        GameDAO result = outboundGameAdapter.createGameForUser(userDAO);

        assertEquals(gameDAO, result);
    }

    @Test
    void getStatusByGameId_GameExists_ReturnsStatus() throws NotFoundException {
        Long gameId = 1L;
        StatusDAO statusDAO = StatusDAO.OPENED;
        GameDAO gameDAO = new GameDAO();
        gameDAO.setStatusDAO(statusDAO);
        when(outboundGameRepositoryPort.findById(gameId)).thenReturn(Optional.of(gameDAO));

        StatusDAO result = outboundGameAdapter.getStatusByGameId(gameId);

        assertEquals(statusDAO, result);
    }

    @Test
    void endGame_GameExists_UpdatesStatus() throws NotFoundException {
        Long gameId = 1L;
        GameDAO gameDAO = new GameDAO();
        when(outboundGameRepositoryPort.findById(gameId)).thenReturn(Optional.of(gameDAO));

        outboundGameAdapter.endGame(gameId);

        assertEquals(StatusDAO.CLOSED, gameDAO.getStatusDAO());
        verify(outboundGameRepositoryPort).save(gameDAO);
    }
}