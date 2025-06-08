package com.shg.battleship_main_server.services;

import com.shg.battleship_main_server.dtos.BoardRescueResponseDto;
import com.shg.battleship_main_server.dtos.Coordinate;
import com.shg.battleship_main_server.dtos.GameRescueRequestDto;
import com.shg.battleship_main_server.dtos.PlayResponseDto;
import com.shg.battleship_main_server.entitys.*;
import com.shg.battleship_main_server.enums.GameStatus;
import com.shg.battleship_main_server.enums.Notification;
import com.shg.battleship_main_server.enums.PlayResult;
import com.shg.battleship_main_server.enums.PlayerStatusForRescue;
import com.shg.battleship_main_server.exceptions.GameNotFoundException;
import com.shg.battleship_main_server.exceptions.RecoverySessionException;
import com.shg.battleship_main_server.repositorys.BoardRepository;
import com.shg.battleship_main_server.repositorys.GameRepository;
import com.shg.battleship_main_server.repositorys.PlayRepository;
import com.shg.battleship_main_server.repositorys.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRecoveryService {

    private final BoardRepository boardRepository;
    private final GameNotificationService gameNotificationService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayRepository playRepository;

    private final Map<UUID, RecoverySession> recoverySessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();



    //  Método para resgatar jogadas de um jogo em andamento
    public synchronized void rescuePlaysGame(Game game, Player player){

        try{
            Player opponent = player.equals(game.getPlayer1())? game.getPlayer2(): game.getPlayer1();

            Board board = boardRepository.findByGameAndPlayer(game, player);
            List<Play> plays = playRepository.findByGame(game);

            if (board == null) {
             throw new EntityNotFoundException("Tabuleiro não encontrado para o jogador no jogo especificado");
            }
            if (plays == null || plays.isEmpty()) {
                throw new EntityNotFoundException("Esse jogo não possui jogadas ");
            }
            Hibernate.initialize(board.getShipPositions());
            List<PlayResponseDto> playResponseDtos = new ArrayList<>();

            for(Play play : plays){
                if(play.getPlayer().getId().equals(player.getId())){
                    PlayResponseDto dto = new PlayResponseDto(play.getResult(), play.getCoordinate(), game.getId(), player.getId(), opponent.getId());
                    playResponseDtos.add(dto);
                }
                if(play.getPlayer().getId().equals(opponent.getId())){
                    PlayResponseDto dto = new PlayResponseDto(play.getResult(), play.getCoordinate(), game.getId(), opponent.getId(), player.getId());
                    playResponseDtos.add(dto);
                }
            }

            Set<Coordinate> shipPositionsSet = board.getShipPositions().stream()
                    .map(ShipPosition::getPosition)
                    .collect(Collectors.toSet());

            gameNotificationService.notifyRescueGame(player.getId(), new BoardRescueResponseDto(playResponseDtos, shipPositionsSet));

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public synchronized void recovery(GameRescueRequestDto data){
        Player player = playerRepository.findById(data.playerId())
                .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));

        Game game = gameRepository.findFirstByPlayersAndGameStatus(player, player, GameStatus.IN_PROGRESS);

        if(data.statusForRescue() == null){
            throw new IllegalArgumentException("Confirmação inválida");
        }

        boolean confirm = data.statusForRescue().equals(PlayerStatusForRescue.YES);

        startRecovery(game.getId(), player.getId());
        confirmRecovery(game.getId(), player.getId(), confirm);
    }

    public synchronized void startRecovery(UUID gameId, UUID playerId){
        if(!recoverySessions.containsKey(gameId)){
            RecoverySession session = new RecoverySession(gameId);
            recoverySessions.put(gameId, session);
            session.startTimeout(() -> cancelGame(gameId, playerId));
        }
    }

    public synchronized void confirmRecovery(UUID gameId, UUID playerId, boolean confirm) {
        RecoverySession session = recoverySessions.get(gameId);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game não encontrado")); // RETORNAR ERROS

        if (session == null) {
            throw new RecoverySessionException("No recovery session found or already expired");
        }

        session.confirm(playerId, confirm);

        if (session.isCancelled()) {
            cancelGame(gameId, playerId);
            recoverySessions.remove(gameId);
            return;
        }

        if (session.isBothConfirmed()) {
            recoverySessions.remove(gameId);
            gameNotificationService.notifyInGameChanges(game.getPlayer1().getId(),Notification.RESUMED);
            gameNotificationService.notifyInGameChanges(game.getPlayer2().getId(),Notification.RESUMED);

            executor.schedule(()->{
                rescuePlaysGame(game, game.getPlayer1());
                rescuePlaysGame(game, game.getPlayer2());
            }, 5, TimeUnit.SECONDS);
            return;
        }

        gameNotificationService.notifyInGameChanges(playerId,Notification.WAITING_OTHER_PLAYER);
    }

    private void cancelGame(UUID gameId, UUID playerId) {
        gameRepository.findById(gameId).ifPresent(game -> {
            game.setGameStatus(GameStatus.CANCELED);
            gameRepository.save(game);
            gameRepository.delete(game);
            gameNotificationService.notifyInGameChanges(playerId, Notification.CANCELLED);
        });
    }
}
