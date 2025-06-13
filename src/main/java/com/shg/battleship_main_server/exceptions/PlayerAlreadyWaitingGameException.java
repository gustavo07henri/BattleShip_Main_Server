package com.shg.battleship_main_server.exceptions;

public class PlayerAlreadyWaitingGameException extends RuntimeException {
    public PlayerAlreadyWaitingGameException(String message) {
        super(message);
    }
}
