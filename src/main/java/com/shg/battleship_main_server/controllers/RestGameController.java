package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.entitys.Board;
import com.shg.battleship_main_server.entitys.Game;
import com.shg.battleship_main_server.dtos.BoardRequestDto;
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

    @PostMapping("/search-game")
    public ResponseEntity<UUID> createGame(@RequestBody UUID playerId){
        Game game = gameService.setGame(playerId);
        return ResponseEntity.status(game.getPlayer2() == null ? HttpStatus.OK : HttpStatus.CREATED).body(game.getId());
    }


    @PostMapping("/create-board")
    public ResponseEntity<Board> createBoard(@RequestBody BoardRequestDto data){
        Board board = gameService.setBoard(data.shipDtos(), data.gameId(), data.playerId());
        System.out.println(data);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }
}
