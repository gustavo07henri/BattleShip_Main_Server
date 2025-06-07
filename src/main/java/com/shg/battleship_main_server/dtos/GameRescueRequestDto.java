package com.shg.battleship_main_server.dtos;

import com.shg.battleship_main_server.enums.PlayerStatusForRescue;

import java.util.UUID;

public record GameRescueRequestDto(
        UUID playerId,
        PlayerStatusForRescue statusForRescue
) {
}
