package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.entitys.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
    Player findByEmail(String email);

    boolean existsPlayerByEmail(String email);
}
