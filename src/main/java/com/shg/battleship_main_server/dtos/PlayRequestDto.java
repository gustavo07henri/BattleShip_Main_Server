package com.shg.battleship_main_server.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.sql.Timestamp;
import java.util.UUID;

public record PlayRequestDto(
        @NotBlank (message = "Campo não pode estar vazio") UUID gameId,
        @Pattern (regexp = "^(?:[A-J](?:10|[1-9]))$\n") Coordinate coordinate,
        Timestamp moment,
        @NotBlank (message = "Campo não pode estar vazio") UUID player
) {}
