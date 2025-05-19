package com.shg.battleship_main_server.dtos;

import java.util.UUID;

public record JoinGameRequest(UUID gameId, UUID playerId) {
}
