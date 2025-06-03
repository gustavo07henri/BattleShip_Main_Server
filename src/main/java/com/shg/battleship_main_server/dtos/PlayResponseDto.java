package com.shg.battleship_main_server.dtos;

import com.shg.battleship_main_server.enums.PlayResult;

import java.util.UUID;

public record PlayResponseDto(
        PlayResult result,
        Coordinate coordinate,
        UUID gameId,
        UUID playerId,
        UUID target) {}
