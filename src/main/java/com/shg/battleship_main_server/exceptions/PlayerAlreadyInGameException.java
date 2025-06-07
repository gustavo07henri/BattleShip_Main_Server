package com.shg.battleship_main_server.exceptions;

public class PlayerAlreadyInGameException extends RuntimeException {
    public PlayerAlreadyInGameException(String message) {
        super(message);
    }
}
