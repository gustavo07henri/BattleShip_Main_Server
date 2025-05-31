package com.shg.battleship_main_server.exceptions;

public class CoordinateAlreadyAttackedException extends RuntimeException {
    public CoordinateAlreadyAttackedException(String message) {
        super(message);
    }
}
