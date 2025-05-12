package com.shg.battleship_main_server.domain;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private int size;
    private int hits;
    private boolean state;


    @ManyToOne
    @JoinColumn(name = "tabuleiro_id")
    private Board board;
}
