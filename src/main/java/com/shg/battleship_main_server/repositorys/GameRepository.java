package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.entitys.Game;
import com.shg.battleship_main_server.entitys.Player;
import com.shg.battleship_main_server.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
    @Query("SELECT COUNT(g) > 0 FROM Game g " +
            "WHERE (g.player1 = :player OR g.player2 = :player) " +
            "AND g.gameStatus IN (:statuses)")
    boolean isPlayerInActiveGame(@Param("player") Player player,
                                 @Param("statuses") List<GameStatus> statuses);

    @Query("""
    SELECT g FROM Game g
    WHERE (g.player1 = :p1 OR g.player2 = :p2)
    AND g.gameStatus = :gameStatus
    ORDER BY g.id DESC
""")
    Game findFirstByPlayersAndGameStatus(
            @Param("p1") Player p1,
            @Param("p2") Player p2,
            @Param("gameStatus") GameStatus gameStatus
    );

    Game findByGameStatus(GameStatus gameStatus);

}
