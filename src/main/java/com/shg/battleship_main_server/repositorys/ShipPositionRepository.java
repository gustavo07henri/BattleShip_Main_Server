package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.domain.Ship;
import com.shg.battleship_main_server.domain.ShipPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipPositionRepository extends JpaRepository<ShipPosition, UUID> {
}
