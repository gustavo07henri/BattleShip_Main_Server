package com.shg.battleship_main_server.dtos;

import com.shg.battleship_main_server.enums.Notification;

import java.util.UUID;

public record NotificationResponseDto(
        Notification notification,
        UUID gameId) {
}
