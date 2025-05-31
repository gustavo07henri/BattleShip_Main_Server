package com.shg.battleship_main_server.services;

import com.shg.battleship_main_server.entitys.Player;
import com.shg.battleship_main_server.dtos.PlayerRequestDto;
import com.shg.battleship_main_server.dtos.PlayerResponseDto;
import com.shg.battleship_main_server.exceptions.EmailAllreadyInUse;
import com.shg.battleship_main_server.repositorys.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerResponseDto create(PlayerRequestDto data){
        Player newPlayer = new Player();
        if(data.name().isEmpty() || data.email().isEmpty()){
            throw new IllegalArgumentException("Os campos não podem estar vazios");
        }
        if(playerRepository.existsPlayerByEmail(data.email())){
            throw new EmailAllreadyInUse("Este email já está em uso.");
        }
        newPlayer.setEmail(data.email());
        newPlayer.setName(data.name());

        Player response = playerRepository.save(newPlayer);
        return new PlayerResponseDto(response.getId());
    }

    public PlayerResponseDto login(PlayerRequestDto data){
        if(data.email().isEmpty()){
            throw new IllegalArgumentException("O campo email, não pode estar vazio");
        }
        Player response = playerRepository.findByEmail(data.email());
        return new PlayerResponseDto(response.getId());
    }
}
