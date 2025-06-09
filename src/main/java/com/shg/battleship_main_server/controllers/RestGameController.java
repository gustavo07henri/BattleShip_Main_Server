package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.dtos.*;
import com.shg.battleship_main_server.entitys.Game;
import com.shg.battleship_main_server.services.GameRecoveryService;
import com.shg.battleship_main_server.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/game/init")
public class RestGameController {

    private final GameService gameService;
    private final GameRecoveryService gameRecoveryService;

    @PostMapping("/search-game")
    public ResponseEntity<GameResponseDto> createGame(@RequestBody GameRequestDto data){
        Game game = gameService.setGame(data.playerId());
        return ResponseEntity.status(game.getPlayer2() == null ? HttpStatus.OK : HttpStatus.CREATED).body(new GameResponseDto(game.getId()));
    }


    @PostMapping("/create-board")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto data){
        BoardResponseDto response = gameService.setBoard(data.shipDtos(), data.gameId(), data.playerId());
        System.out.println(data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/rescue-game")
    public ResponseEntity<String> rescueGame(@RequestBody GameRescueRequestDto data){
        gameRecoveryService.recovery(data);
        return ResponseEntity.ok().body("");
    }
}
