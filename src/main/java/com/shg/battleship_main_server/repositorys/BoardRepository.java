package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.entitys.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    boolean existsByGameIdAndPlayerId(UUID id, UUID id1);
}
