package com.shg.battleship_main_server.gameCore;

import com.shg.battleship_main_server.domain.*;
import com.shg.battleship_main_server.domain.dtos.PlayDto;
import com.shg.battleship_main_server.domain.dtos.RequestShipDto;
import com.shg.battleship_main_server.domain.enums.GameStatus;
import com.shg.battleship_main_server.domain.enums.PlayResult;
import com.shg.battleship_main_server.domain.enums.ShipState;
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

    public Game SetGame(UUID player1Id, UUID player2Id){
        if(player1Id.equals(player2Id)){
            throw new IllegalArgumentException("Os jogadores devem ser diferentes");
        }
        Player p1 = playerRepository.findById(player1Id)
                .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));
        Player p2 = playerRepository.findById(player2Id)
                .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));

        if(gameRepository.existsByPlayer1OrPlayer2AndGameStatus(p1, p2, GameStatus.IN_PROGRESS)){
            return gameRepository.findByPlayer1OrPlayer2AndGameStatus(p1, p2, GameStatus.IN_PROGRESS);
        }
        long timestamp = Instant.now().getEpochSecond();

        Game newGame = new Game();
        newGame.setGameStatus(GameStatus.IN_PROGRESS);
        newGame.setStart(new Timestamp(timestamp));
        newGame.setPlayer1(p1);
        newGame.setPlayer2(p2);
        newGame.setCurrentPlayer(p1);
        return gameRepository.save(newGame);
    }

    public Board setBoard(List<RequestShipDto> data, UUID gameId, UUID playerId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Jogador não encontrado"));

        if(!player.equals(game.getPlayer1()) && !player.equals(game.getPlayer2())){
            throw new IllegalStateException("Jogador não pertence a partida");
        }
        validateShips(data);

        Board board = new Board();
        board.setGame(game);
        board.setPlayer(player);
        List<Ship> ships = new ArrayList<>();
        List<ShipPosition> shipPositions = new ArrayList<>();

        for(RequestShipDto dto : data){
            Ship newShip = new Ship();
            newShip.setBoard(board);
            newShip.setState(ShipState.AFLOAT);
            newShip.setSize(dto.coordinates().size());
            newShip.setHits(0);
            ships.add(newShip);

            for(Coordinate c : dto.coordinates()){
                ShipPosition sp = new ShipPosition();
                sp.setPosition(c);
                sp.setShip(newShip);
                shipPositions.add(sp);
            }
        }


        board.setShips(ships);
        board.setShipPositions(shipPositions);
        board.setAttacksReceived(new ArrayList<>());

        return boardRepository.save(board);

    }
    public void validateShips(List<RequestShipDto> data){
        int[] allowedShipSizes = {5,4,3,3,2,2,2,1};
        int maxShips = allowedShipSizes.length;

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
    public PlayResult setPlay(PlayDto data, UUID boardId){
        var board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Tabuleriro não encontrado"));
        var player = playerRepository.findById(data.player())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Game game = board.getGame();

        if(game.getGameStatus() != GameStatus.IN_PROGRESS){
            throw new IllegalStateException("O jogo não esta em andamento");
        }
        if(board.getAttacksReceived().contains(data.coordinate())){
            throw new IllegalArgumentException("Cordenada já atacada");
        }
        if(!player.equals(game.getCurrentPlayer())){
            throw new IllegalStateException("Não é o turno desse jogador");
        }
        if(player.equals(board.getPlayer())){
            throw new IllegalStateException("Não é possivel atacar o proprio tabuleiro");
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

        if(checkGame(boardId) == GameStatus.FINISHED){
            defineWinner(board, player);
            gameRepository.save(game);
        }else{
            game.setCurrentPlayer(player.equals(game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1());
            gameRepository.save(game);
        }

        return play.getResult();
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
