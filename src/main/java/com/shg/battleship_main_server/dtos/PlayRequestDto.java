package com.shg.battleship_main_server.dtos;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayRequestDto(
        UUID gameId,
        Coordinate coordinate,
        Timestamp moment,
        UUID player
) {}
