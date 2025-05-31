package com.shg.battleship_main_server.dtos;

public record ErrorMessage(
        String error,
        String message,
        String errorCode
) {
}
