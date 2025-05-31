package com.shg.battleship_main_server.exceptions;

public class EmailAllreadyInUse extends RuntimeException {
    public EmailAllreadyInUse(String message) {
        super(message);
    }
}
