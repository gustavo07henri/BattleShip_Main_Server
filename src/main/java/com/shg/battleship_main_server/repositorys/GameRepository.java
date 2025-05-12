package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
}
