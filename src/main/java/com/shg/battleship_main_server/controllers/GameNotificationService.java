package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.dtos.GameRequestDto;
import com.shg.battleship_main_server.dtos.GameResponseDto;
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
}
