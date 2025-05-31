package com.shg.battleship_main_server.exceptions;

public class SelfAttackNotAllowedException extends RuntimeException {
    public SelfAttackNotAllowedException(String message) {
        super(message);
    }
}
