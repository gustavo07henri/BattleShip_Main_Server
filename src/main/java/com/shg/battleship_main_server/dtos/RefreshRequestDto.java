package com.shg.battleship_main_server.dtos;

import java.util.UUID;

public record RefreshRequestDto(
        UUID playerId,
        UUID gameId
) {
}
