package com.shg.battleship_main_server.domain.dtos;

import com.shg.battleship_main_server.domain.Coordinate;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayDto(
        Coordinate coordinate,
        Timestamp moment,
        UUID player
) {}
