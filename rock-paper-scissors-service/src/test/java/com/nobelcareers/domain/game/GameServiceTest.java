package com.nobelcareers.domain.game;

import com.nobelcareers.domain.game.bo.MovementValueBO;
import com.nobelcareers.domain.game.bo.WinnerBO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameServiceTest {

    private final GameService gameService = new GameService();

    @Test
    void getWinner_Draw() {
        assertEquals(WinnerBO.DRAW, gameService.getWinner(MovementValueBO.ROCK, MovementValueBO.ROCK));
        assertEquals(WinnerBO.DRAW, gameService.getWinner(MovementValueBO.PAPER, MovementValueBO.PAPER));
        assertEquals(WinnerBO.DRAW, gameService.getWinner(MovementValueBO.SCISSORS, MovementValueBO.SCISSORS));
    }

    @Test
    void getWinner_PlayerWinsWithPaperAgainstRock() {
        assertEquals(WinnerBO.PLAYER, gameService.getWinner(MovementValueBO.PAPER, MovementValueBO.ROCK));
    }

    @Test
    void getWinner_PlayerWinsWithRockAgainstScissors() {
        assertEquals(WinnerBO.PLAYER, gameService.getWinner(MovementValueBO.ROCK, MovementValueBO.SCISSORS));
    }

    @Test
    void getWinner_PlayerWinsWithScissorsAgainstPaper() {
        assertEquals(WinnerBO.PLAYER, gameService.getWinner(MovementValueBO.SCISSORS, MovementValueBO.PAPER));
    }

    @Test
    void getWinner_ServerWinsWithRockAgainstScissors() {
        assertEquals(WinnerBO.SERVER, gameService.getWinner(MovementValueBO.SCISSORS, MovementValueBO.ROCK));
    }

    @Test
    void getWinner_ServerWinsWithScissorsAgainstPaper() {
        assertEquals(WinnerBO.SERVER, gameService.getWinner(MovementValueBO.PAPER, MovementValueBO.SCISSORS));
    }

    @Test
    void getWinner_ServerWinsWithPaperAgainstRock() {
        assertEquals(WinnerBO.SERVER, gameService.getWinner(MovementValueBO.ROCK, MovementValueBO.PAPER));
    }
}