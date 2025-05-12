package com.shg.battleship_main_server.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Object, UUID> {
}
