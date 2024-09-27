package com.srmasset.adapters.outbound.database.exception;

public class GameNotFound extends RuntimeException {
    public GameNotFound() {
        super("Game not found");
    }
}
