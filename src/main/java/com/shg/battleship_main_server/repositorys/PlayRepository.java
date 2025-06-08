package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.entitys.Game;
import com.shg.battleship_main_server.entitys.Play;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlayRepository extends JpaRepository<Play, UUID> {
    List<Play> findByGame(Game game);
}
