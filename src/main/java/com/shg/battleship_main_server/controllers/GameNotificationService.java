package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.dtos.GameRequestDto;
import com.shg.battleship_main_server.dtos.GameResponseDto;
import com.shg.battleship_main_server.dtos.NotificationResponseDto;
import com.shg.battleship_main_server.enums.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GameNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Método dedicado a notificar jogador do início do jogo
     * @param playerId
     * @param gameId
     */
    public void notifyPlayerGameStarted(UUID playerId, UUID gameId){
        messagingTemplate.convertAndSend("/topics/game-started/" + playerId, new GameResponseDto(gameId));
    }

    public void notifyInGameChanges(UUID playerId, Notification notification){
        messagingTemplate.convertAndSend("/topics/game-notify/" + playerId, new NotificationResponseDto(notification));
    }
}
