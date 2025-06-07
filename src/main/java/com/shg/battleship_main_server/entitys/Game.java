package com.shg.battleship_main_server.entitys;

import com.shg.battleship_main_server.enums.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_jogo")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
    private Timestamp start;
    private Timestamp finish;

    @ManyToOne
    @JoinColumn(name = "player_1_id")
    private Player player1;


    @ManyToOne
    @JoinColumn(name = "player_2_id")
    private Player player2;

    @ManyToOne
    @JoinColumn(name = "vencedor_id")
    private Player winner;

    @ManyToOne
    @JoinColumn(name = "jogador_atual_id")
    private Player currentPlayer;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

}
