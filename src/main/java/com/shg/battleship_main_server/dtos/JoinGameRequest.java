package com.shg.battleship_main_server.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record JoinGameRequest(
        @NotBlank (message = "Campo não pode estar vazio")UUID gameId,
        @NotBlank (message = "Campo não pode estar vazio")UUID playerId) {
}
