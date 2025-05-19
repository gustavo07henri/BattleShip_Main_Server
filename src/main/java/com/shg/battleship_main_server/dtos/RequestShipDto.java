package com.shg.battleship_main_server.dtos;

import java.util.List;

public record RequestShipDto(
        List<Coordinate> coordinates
) {}
