package com.shg.battleship_main_server.entitys;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shg.battleship_main_server.dtos.Coordinate;
import com.shg.battleship_main_server.utils.CoordinateConverter;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Transient
    private final int height = 10;
    @Transient
    private final int width = 10;
    private boolean isVisible = false;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ship> ships;

    @OneToMany
    private List<ShipPosition> shipPositions;

    @ElementCollection
    @Convert(converter = CoordinateConverter.class)
    @CollectionTable(name = "tb_cordinates", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "ataques_recebidos")
    private List<Coordinate> attacksReceived;

    @ManyToOne
    @JoinColumn(name = "jogador_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Game game;

}
