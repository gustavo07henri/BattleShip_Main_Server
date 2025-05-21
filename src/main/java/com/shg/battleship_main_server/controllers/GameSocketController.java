package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.dtos.PlayRequestDto;
import com.shg.battleship_main_server.dtos.PlayResponseDto;
import com.shg.battleship_main_server.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameSocketController {

    private final GameService gameService;

    @MessageMapping("/new-play")
    @SendTo("/topics/play")
    public PlayResponseDto newPlay(PlayRequestDto data) {
        try {
            return gameService.setPlay(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
