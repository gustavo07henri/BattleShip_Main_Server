package com.shg.battleship_main_server.dtos;

import java.util.List;
import java.util.UUID;

public record BoardRequestDto(
        List<RequestShipDto> shipDtos,
        UUID playerId,
        UUID gameId
) { }
