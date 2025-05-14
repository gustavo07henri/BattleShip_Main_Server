package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.domain.Game;
import com.shg.battleship_main_server.domain.Player;
import com.shg.battleship_main_server.domain.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    boolean existsByPlayer1OrPlayer2AndGameStatus(Player p1, Player p2, GameStatus gameStatus);

    Game findByPlayer1OrPlayer2AndGameStatus(Player p1, Player p2, GameStatus gameStatus);
}
