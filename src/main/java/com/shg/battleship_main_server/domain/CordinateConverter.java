package com.shg.battleship_main_server.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CordinateConverter implements AttributeConverter<Coordinate, String> {

    @Override
    public String convertToDatabaseColumn(Coordinate coordinate) {
        return coordinate.toString();
    }

    @Override
    public Coordinate convertToEntityAttribute(String s) {
        return Coordinate.fromString(s);
    }
}
