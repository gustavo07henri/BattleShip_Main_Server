package com.shg.battleship_main_server.entitys;

import com.shg.battleship_main_server.enums.ShipState;
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
@Table(name = "tb_navio")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int size;
    private int hits;

    @Enumerated(EnumType.STRING)
    private ShipState state;

    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipPosition> positions;

    @ManyToOne
    @JoinColumn(name = "tabuleiro_id")
    private Board board;
}
