package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.entitys.Board;
import com.shg.battleship_main_server.entitys.Game;
import com.shg.battleship_main_server.entitys.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    boolean existsByGameIdAndPlayerId(UUID id, UUID id1);

    @EntityGraph(attributePaths = {"attacksReceived", "shipPositions"})
    Board findByGameAndPlayer(Game game, Player player);
}
