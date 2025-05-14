package com.shg.battleship_main_server.domain;

import com.shg.battleship_main_server.domain.enums.ShipState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_navio")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int size;
    private int hits;
    private ShipState state;


    @ManyToOne
    @JoinColumn(name = "tabuleiro_id")
    private Board board;
}
