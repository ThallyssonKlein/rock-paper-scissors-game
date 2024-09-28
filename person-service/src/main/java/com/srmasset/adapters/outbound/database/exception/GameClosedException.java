package com.srmasset.adapters.outbound.database.exception;

public class GameClosedException extends RuntimeException {
    public GameClosedException() {
        super("Game closed");
    }
}
