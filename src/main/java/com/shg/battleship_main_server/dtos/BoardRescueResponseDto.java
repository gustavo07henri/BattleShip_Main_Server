package com.shg.battleship_main_server.dtos;

import java.util.List;
import java.util.Set;

public record BoardRescueResponseDto(
        List<PlayResponseDto> plays,
        Set<Coordinate> shipPositions
) {

}
