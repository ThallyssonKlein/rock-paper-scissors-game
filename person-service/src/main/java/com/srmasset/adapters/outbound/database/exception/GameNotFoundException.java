package com.srmasset.adapters.outbound.database.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException() {
        super("Game not found");
    }
}
