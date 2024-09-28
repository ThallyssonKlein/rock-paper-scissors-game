package com.nobelcareers.domain.game;

import com.nobelcareers.domain.game.bo.MovementValueBO;
import com.nobelcareers.domain.game.bo.WinnerBO;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    public WinnerBO getWinner(MovementValueBO playerMove, MovementValueBO serverMove) {
        if (playerMove.equals(serverMove)) {
            return WinnerBO.DRAW;
        } else if (playerMove.equals(MovementValueBO.PAPER) && serverMove.equals(MovementValueBO.ROCK)) {
            return WinnerBO.PLAYER;
        } else if (playerMove.equals(MovementValueBO.ROCK) && serverMove.equals(MovementValueBO.SCISSORS)) {
            return WinnerBO.PLAYER;
        } else if (playerMove.equals(MovementValueBO.SCISSORS) && serverMove.equals(MovementValueBO.PAPER)) {
            return WinnerBO.PLAYER;
        } else {
            return WinnerBO.SERVER;
        }
    }
}
