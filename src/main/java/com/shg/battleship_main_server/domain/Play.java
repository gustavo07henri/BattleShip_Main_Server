package com.shg.battleship_main_server.domain;

import com.shg.battleship_main_server.domain.enums.PlayResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_jogada")
public class Play {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private PlayResult result;

    @Convert(converter = CordinateConverter.class)
    private Coordinate coordinate;
    private Timestamp moment;


    @ManyToOne
    @JoinColumn(name = "jogador_id")
    private Player player;


    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Game game;
}
