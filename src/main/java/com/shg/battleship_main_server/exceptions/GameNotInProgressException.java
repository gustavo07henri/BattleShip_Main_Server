package com.shg.battleship_main_server.exceptions;

public class GameNotInProgressException extends RuntimeException {
    public GameNotInProgressException(String message) {
        super(message);
    }
}
