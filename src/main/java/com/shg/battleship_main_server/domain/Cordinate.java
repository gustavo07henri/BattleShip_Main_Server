package com.shg.battleship_main_server.domain;

import lombok.Getter;

import java.util.Objects;

public record Cordinate(int row, int col) {
    public Cordinate{
        if(row < 0 || row >= 10 || col < 0 || col >= 10){
            throw new IllegalArgumentException("Coordenada fora dos limites do tabuleiro (10x10).");
        }
    }

    public static Cordinate fromString(String coord){
        if(coord == null || coord.length() < 2 || coord.length() > 3){
            throw new IllegalArgumentException("Formato de coordenada inválido (esperado A1 - J10)");
        }
        char letter = Character.toUpperCase(coord.charAt(0));

        int row = letter - 'A';
        int col;

        try {
            col = Integer.parseInt(coord.substring(1)) -1;
        }catch (NumberFormatException ex){
            throw  new IllegalArgumentException("Número da coordenada inválido.");
        }
        return new Cordinate(col, row);
    }

    @Override
    public String toString(){
        return "" + (char)(row + 'A') + (col + 1);
    }


}
