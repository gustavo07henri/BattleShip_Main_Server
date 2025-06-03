package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.dtos.PlayerRequestDto;
import com.shg.battleship_main_server.dtos.PlayerResponseDto;
import com.shg.battleship_main_server.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game/players")
public class PlayerController {

    private final PlayerService playerService;
    
    @PostMapping("signup")
    public ResponseEntity<PlayerResponseDto> create (@RequestBody PlayerRequestDto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(data));
    }

    @PostMapping("login")
    public ResponseEntity<PlayerResponseDto> login (@RequestBody PlayerRequestDto data){
        return ResponseEntity.status(HttpStatus.OK).body(playerService.login(data));
    }
}
