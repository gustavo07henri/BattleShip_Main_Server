package com.shg.battleship_main_server.dtos;

import com.shg.battleship_main_server.entitys.ShipPosition;


import java.util.Set;
import java.util.UUID;

public record BoardResponseDto(
        UUID playerId,
        UUID gameId,
        Set<ShipPosition> positions,
        Set<Coordinate> attacksReceived
) {
}
