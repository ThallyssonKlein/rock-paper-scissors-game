package com.nobelcareers.domain.game;

import com.nobelcareers.adapters.outbound.database.OutboundMovementAdapter;
import com.nobelcareers.domain.game.bo.MovementBO;
import com.nobelcareers.domain.game.bo.MovementValueBO;
import com.nobelcareers.domain.game.bo.WinnerBO;
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

public class GameServiceTest {

    @Mock
    private OutboundMovementAdapter outboundMovementAdapter;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateServerMovement_PlayerHasMorePaper_ServerChoosesScissors() {
        List<MovementBO> movements = Arrays.asList(
                new MovementBO(MovementValueBO.PAPER),
                new MovementBO(MovementValueBO.PAPER),
                new MovementBO(MovementValueBO.ROCK)
        );
        when(outboundMovementAdapter.findAllMovementsFromOnePlayer(1L)).thenReturn(movements);

        String result = gameService.generateServerMovement(1L);

        assertEquals(MovementValueBO.SCISSORS.name(), result);
    }

    @Test
    void generateServerMovement_PlayerHasMoreRock_ServerChoosesPaper() {
        List<MovementBO> movements = Arrays.asList(
                new MovementBO(MovementValueBO.ROCK),
                new MovementBO(MovementValueBO.ROCK),
                new MovementBO(MovementValueBO.SCISSORS)
        );
        when(outboundMovementAdapter.findAllMovementsFromOnePlayer(1L)).thenReturn(movements);

        String result = gameService.generateServerMovement(1L);

        assertEquals(MovementValueBO.PAPER.name(), result);
    }

    @Test
    void generateServerMovement_PlayerHasMoreScissors_ServerChoosesRock() {
        List<MovementBO> movements = Arrays.asList(
                new MovementBO(MovementValueBO.SCISSORS),
                new MovementBO(MovementValueBO.SCISSORS),
                new MovementBO(MovementValueBO.PAPER)
        );
        when(outboundMovementAdapter.findAllMovementsFromOnePlayer(1L)).thenReturn(movements);

        String result = gameService.generateServerMovement(1L);

        assertEquals(MovementValueBO.ROCK.name(), result);
    }

    @Test
    void generateServerMovement_PlayerHasNoMovements_ServerChoosesRandom() {
        when(outboundMovementAdapter.findAllMovementsFromOnePlayer(1L)).thenReturn(Collections.emptyList());

        String result = gameService.generateServerMovement(1L);

        List<String> possibleResults = Arrays.asList(MovementValueBO.PAPER.name(), MovementValueBO.ROCK.name(), MovementValueBO.SCISSORS.name());
        assertEquals(true, possibleResults.contains(result));
    }

    @Test
    void getWinner_PlayerAndServerSameMove_Draw() {
        WinnerBO result = gameService.getWinner(MovementValueBO.ROCK, MovementValueBO.ROCK);

        assertEquals(WinnerBO.DRAW, result);
    }

    @Test
    void getWinner_PlayerPaperServerRock_PlayerWins() {
        WinnerBO result = gameService.getWinner(MovementValueBO.PAPER, MovementValueBO.ROCK);

        assertEquals(WinnerBO.PLAYER, result);
    }

    @Test
    void getWinner_PlayerRockServerScissors_PlayerWins() {
        WinnerBO result = gameService.getWinner(MovementValueBO.ROCK, MovementValueBO.SCISSORS);

        assertEquals(WinnerBO.PLAYER, result);
    }

    @Test
    void getWinner_PlayerScissorsServerPaper_PlayerWins() {
        WinnerBO result = gameService.getWinner(MovementValueBO.SCISSORS, MovementValueBO.PAPER);

        assertEquals(WinnerBO.PLAYER, result);
    }

    @Test
    void getWinner_PlayerRockServerPaper_ServerWins() {
        WinnerBO result = gameService.getWinner(MovementValueBO.ROCK, MovementValueBO.PAPER);

        assertEquals(WinnerBO.SERVER, result);
    }
}