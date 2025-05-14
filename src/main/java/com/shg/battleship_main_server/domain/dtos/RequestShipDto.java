package com.shg.battleship_main_server.domain.dtos;

import com.shg.battleship_main_server.domain.Coordinate;

import java.util.List;

public record RequestShipDto(
        List<Coordinate> coordinates
) {}
