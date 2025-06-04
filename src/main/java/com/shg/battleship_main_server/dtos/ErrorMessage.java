package com.shg.battleship_main_server.dtos;

public record ErrorMessage(
        String err,
        String msg,
        String errorCode
) {
}
