package com.shg.battleship_main_server.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestShipDto(
        @NotNull(message = "Esse campo não pode ser null") List<Coordinate> coordinates
) {}
