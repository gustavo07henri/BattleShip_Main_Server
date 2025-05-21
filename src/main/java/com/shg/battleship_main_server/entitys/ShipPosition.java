package com.shg.battleship_main_server.entitys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shg.battleship_main_server.dtos.Coordinate;
import com.shg.battleship_main_server.utils.CoordinateConverter;
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

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Convert(converter = CoordinateConverter.class)
    private Coordinate position;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "navio_id")
    private Ship ship;
}
