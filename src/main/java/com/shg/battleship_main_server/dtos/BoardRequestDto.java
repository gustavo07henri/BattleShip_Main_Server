package com.shg.battleship_main_server.dtos;


import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record BoardRequestDto(
        List<RequestShipDto> shipDtos,
        @NotBlank UUID playerId,
        @NotBlank UUID gameId
) { }
