package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
}
