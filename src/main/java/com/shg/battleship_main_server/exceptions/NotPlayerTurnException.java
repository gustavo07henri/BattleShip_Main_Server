package com.shg.battleship_main_server.exceptions;

public class NotPlayerTurnException extends RuntimeException {
    public NotPlayerTurnException(String message) {
        super(message);
    }
}
