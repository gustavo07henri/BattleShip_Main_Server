package com.shg.battleship_main_server.repositorys;

import com.shg.battleship_main_server.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipRepository extends JpaRepository<Ship, UUID> {
}
