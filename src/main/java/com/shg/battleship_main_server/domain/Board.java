package com.shg.battleship_main_server.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_tabuleiro")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Transient
    private final int height = 10;
    @Transient
    private final int width = 10;
    private boolean isVisible = false;

    @OneToMany
    private List<Ship> ships;

    @OneToMany
    private List<ShipPosition> shipPositions;

    @ElementCollection
    @Convert(converter = CordinateConverter.class)
    @CollectionTable(name = "tb_cordinates", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "ataques_recebidos")
    private List<Cordinate> attacksReceived;

    @ManyToOne
    @JoinColumn(name = "jogador_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Game game;

}
