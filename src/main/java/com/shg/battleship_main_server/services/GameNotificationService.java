package com.shg.battleship_main_server.services;

import com.shg.battleship_main_server.dtos.BoardRescueResponseDto;
import com.shg.battleship_main_server.dtos.GameResponseDto;
import com.shg.battleship_main_server.dtos.NotificationResponseDto;
import com.shg.battleship_main_server.dtos.PlayResponseDto;
import com.shg.battleship_main_server.enums.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void notifyInGameChanges(UUID playerId, Notification notification, UUID gameId){
        messagingTemplate.convertAndSend("/topics/game-notify/" + playerId, new NotificationResponseDto(notification, gameId));
    }

    public void notifyRescueGame(UUID playerId, BoardRescueResponseDto response){
        messagingTemplate.convertAndSend("/topics/game-rescue/" + playerId, response);
    }
}
