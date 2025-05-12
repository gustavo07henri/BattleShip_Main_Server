package com.shg.battleship_main_server.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_posicao_navio")
public class ShipPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Convert(converter = CordinateConverter.class)
    private Cordinate position;

    @ManyToOne
    @JoinColumn(name = "navio_id")
    private Ship ship;
}
