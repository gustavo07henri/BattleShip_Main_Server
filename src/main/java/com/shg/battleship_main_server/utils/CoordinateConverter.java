package com.shg.battleship_main_server.utils;

import com.shg.battleship_main_server.dtos.Coordinate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CoordinateConverter implements AttributeConverter<Coordinate, String> {

    @Override
    public String convertToDatabaseColumn(Coordinate coordinate) {
        return coordinate.toString();
    }

    @Override
    public Coordinate convertToEntityAttribute(String s) {
        return Coordinate.fromString(s);
    }
}
