package com.shg.battleship_main_server.services;

import com.shg.battleship_main_server.controllers.GameNotificationService;
import com.shg.battleship_main_server.dtos.*;
import com.shg.battleship_main_server.entitys.*;
import com.shg.battleship_main_server.enums.GameStatus;
import com.shg.battleship_main_server.enums.Notification;
import com.shg.battleship_main_server.enums.PlayResult;
import com.shg.battleship_main_server.enums.ShipState;
import com.shg.battleship_main_server.exceptions.*;
import com.shg.battleship_main_server.repositorys.BoardRepository;
import com.shg.battleship_main_server.repositorys.GameRepository;
import com.shg.battleship_main_server.repositorys.PlayRepository;
import com.shg.battleship_main_server.repositorys.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class  GameService{

    private final GameRepository gameRepository;
    private final BoardRepository boardRepository;
    private final PlayerRepository playerRepository;
    private final PlayRepository playRepository;
    private final GameNotificationService gameNotificationService;

    public Game setGame(UUID playerId){
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));

        if (gameRepository.existsByPlayer1OrPlayer2AndGameStatus(player, player, GameStatus.WAITING) ||
                gameRepository.existsByPlayer1OrPlayer2AndGameStatus(player, player, GameStatus.IN_PROGRESS)) {
            throw new IllegalStateException("Jogador já está em um jogo ativo");
        }

        Game game = gameRepository.findByGameStatus(GameStatus.WAITING);
        if (game != null && !game.getPlayer1().equals(player)) {
            return joinGame(game.getId(), playerId);
        }


        long timestamp = Instant.now().getEpochSecond();

        Game newGame = new Game();
        newGame.setGameStatus(GameStatus.WAITING);
        newGame.setStart(new Timestamp(timestamp));
        newGame.setPlayer1(player);
        newGame.setCurrentPlayer(player);


        return gameRepository.save(newGame);
    }

    public Game joinGame(UUID gameId, UUID playerId){
        System.out.printf("Game id:  %s, player Id: %s ", gameId, playerId);
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
        Player p = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Jogador 2 não encontrado"));

        if (game.getPlayer1().equals(p)){
            throw new IllegalArgumentException("Os jogadores devem ser diferentes");
        }
        if(game.getGameStatus() != GameStatus.WAITING){
            throw new IllegalStateException("Jogo não está aguardando jogadores");
        }
        if(gameRepository.existsByPlayer1OrPlayer2AndGameStatus(p, p, GameStatus.IN_PROGRESS)){
            throw new IllegalStateException("Jogador já está em um jogo ativo");
        }

        game.setPlayer2(p);
        game.setGameStatus(GameStatus.IN_PROGRESS);

        // Notifica jogadores do início do jogo
        gameNotificationService.notifyPlayerGameStarted(game.getPlayer1().getId(), game.getId());
        gameNotificationService.notifyPlayerGameStarted(game.getPlayer2().getId(), game.getId());

        gameNotificationService.notifyInGameChanges(game.getPlayer1().getId(), Notification.YOUR_TURN);
        gameNotificationService.notifyInGameChanges(game.getPlayer2().getId(), Notification.NOT_YOU_TURN);

        return gameRepository.save(game);
    }

    public BoardResponseDto setBoard(List<RequestShipDto> data, UUID gameId, UUID playerId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));

        if(!player.equals(game.getPlayer1()) && !player.equals(game.getPlayer2())){
            throw new IllegalStateException("Jogador não pertence a partida");
        }
        if(game.getBoards().size() >= 2 || boardRepository.existsByGameIdAndPlayerId(game.getId(), player.getId())){
            throw new IllegalArgumentException("não se pode associar mais tabuleiros a esse jogo");
        }
        validateShips(data);

        Board board = new Board();
        board.setGame(game);
        board.setPlayer(player);
        List<Ship> ships = new ArrayList<>();
        List<ShipPosition> shipPositions = new ArrayList<>();

        for(RequestShipDto dto : data){
            System.out.println("dto: "+dto.toString());
            System.out.println("coordinates: "+dto.coordinates().toString());
            Ship newShip = new Ship();
            newShip.setBoard(board);
            newShip.setState(ShipState.AFLOAT);
            newShip.setSize(dto.coordinates().size());
            newShip.setHits(0);
            newShip.setPositions(new ArrayList<>());
            ships.add(newShip);

            for(Coordinate c : dto.coordinates()){
                ShipPosition sp = new ShipPosition();
                sp.setPosition(c);
                sp.setShip(newShip);

                shipPositions.add(sp);
                newShip.getPositions().add(sp);
            }

        }


        board.setShips(ships);
        board.setShipPositions(shipPositions);
        board.setAttacksReceived(new ArrayList<>());

        boardRepository.save(board);

        return  new BoardResponseDto(
                board.getPlayer().getId(),
                board.getGame().getId(),
                board.getShipPositions(),
                board.getAttacksReceived()
        );

    }
    public void validateShips(List<RequestShipDto> data){
        int[] allowedShipSizes = {5,4,3,3,2};
        int maxShips = allowedShipSizes.length;
        System.out.println(data);

        if(data.size() > maxShips || data.isEmpty()){
            throw new IllegalArgumentException("Quantidade de navios deve ser entre 1 e " + maxShips);
        }

        List<Integer> shipSizes = data.stream().map(dto -> dto.coordinates().size()).sorted().toList();
        List<Integer> allowedSizes = Arrays.stream(allowedShipSizes).boxed().sorted().toList();
        if (!shipSizes.equals(allowedSizes)) {
            throw new IllegalArgumentException("Tamanhos dos navios devem ser " + Arrays.toString(allowedShipSizes));
        }

        Set<Coordinate> coordinateSet = new HashSet<>();
        for(RequestShipDto dto: data){
            for(Coordinate c : dto.coordinates()){
                if(!coordinateSet.add(c)){
                    throw new IllegalArgumentException("Sobreposição de navios detectada na coordenada " + c);
                }
            }
        }


        for(RequestShipDto dto : data){
            List<Coordinate> coordinates = dto.coordinates();
            if(coordinates.size() > 1){
                boolean isHorizontal = coordinates.stream().allMatch(c -> c.row() == coordinates.get(0).row());
                boolean isVertical = coordinates.stream().allMatch(c -> c.col() == coordinates.get(0).col());

                if (isHorizontal) {
                    List<Coordinate> sorted = coordinates.stream()
                            .sorted(Comparator.comparingInt(Coordinate::col))
                            .toList();
                    for (int i = 1; i < sorted.size(); i++) {
                        if (sorted.get(i).col() != sorted.get(i - 1).col() + 1) {
                            throw new IllegalArgumentException("Coordenadas do navio não são contíguas: " + sorted);
                        }
                    }
                } else if (isVertical) {
                    List<Coordinate> sorted = coordinates.stream()
                            .sorted(Comparator.comparingInt(Coordinate::row))
                            .toList();
                    for (int i = 1; i < sorted.size(); i++) {
                        if (sorted.get(i).row() != sorted.get(i - 1).row() + 1) {
                            throw new IllegalArgumentException("Coordenadas do navio não são contíguas: " + sorted);
                        }
                    }
                }
            }
        }


    }

    @Transactional
    public PlayResponseDto setPlay(PlayRequestDto data){

        var game = gameRepository.findById(data.gameId())
                .orElseThrow(() -> new UserNotFoundException("Jogo não encontrado"));
        var player = playerRepository.findById(data.player())
                .orElseThrow(() -> new GameNotFoundException("Usuário não encontrado"));

        Board board = game.getBoards().stream()
                .filter(b -> b.getPlayer() != player)
                .findFirst()
                .orElse(null);


        if(game.getGameStatus() != GameStatus.IN_PROGRESS){
            throw new GameNotInProgressException("O jogo não esta em andamento");
        }
        assert board != null;
        if(board.getAttacksReceived().contains(data.coordinate())){
            throw new CoordinateAlreadyAttackedException("Coordenada já atacada");
        }
        if(!player.equals(game.getCurrentPlayer())){
            throw new NotPlayerTurnException("Não é o turno desse jogador");
        }
        if(player.equals(board.getPlayer())){
            throw new SelfAttackNotAllowedException("Não é possível atacar o próprio tabuleiro");
        }

        Play play = new Play();
        play.setCoordinate(data.coordinate());
        play.setMoment(new Timestamp(Instant.now().toEpochMilli()));
        play.setPlayer(player);
        play.setGame(game);
        play.setResult(PlayResult.MISS);


        for (ShipPosition sp : board.getShipPositions()) {
            if (sp.getPosition().equals(data.coordinate())) {
                play.setResult(PlayResult.HIT);
                Ship ship = sp.getShip();
                ship.setHits(ship.getHits() + 1);
                if (ship.getHits() >= ship.getSize()) {
                    ship.setState(ShipState.SUNKEN);
                }
                break;
            }
        }

        board.getAttacksReceived().add(data.coordinate());
        boardRepository.save(board);
        playRepository.save(play);

        Player opponent = player.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();

        if(checkGame(board.getId()) == GameStatus.FINISHED){
            defineWinner(board, player);
            gameRepository.save(game);

            gameNotificationService.notifyInGameChanges(player.getId(), Notification.WINNER);
            gameNotificationService.notifyInGameChanges(opponent.getId(), Notification.LOSER);
        }else{
            game.setCurrentPlayer(opponent);
            gameRepository.save(game);
            gameNotificationService.notifyInGameChanges(player.getId(), Notification.NOT_YOU_TURN);
            gameNotificationService.notifyInGameChanges(opponent.getId(), Notification.YOUR_TURN);
        }

        return new PlayResponseDto(
                play.getResult(),
                play.getCoordinate(),
                play.getGame().getId(),
                play.getPlayer().getId(),
                opponent.getId());
    }

    public GameStatus checkGame(UUID boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Tabuleiro não encontrado"));

        boolean allSunk = board.getShips().stream()
                .allMatch(ship -> ship.getState() == ShipState.SUNKEN);

        return allSunk ? GameStatus.FINISHED : GameStatus.IN_PROGRESS;
    }

    private void defineWinner(Board board, Player winner) {
        Game game = board.getGame();
        game.setWinner(winner);
        game.setFinish(new Timestamp(Instant.now().toEpochMilli()));
        game.setGameStatus(GameStatus.FINISHED);
        gameRepository.save(game);
    }


}
