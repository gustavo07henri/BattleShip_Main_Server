package com.shg.battleship_main_server.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CordinateConverter implements AttributeConverter<Cordinate, String> {

    @Override
    public String convertToDatabaseColumn(Cordinate cordinate) {
        return cordinate.toString();
    }

    @Override
    public Cordinate convertToEntityAttribute(String s) {
        return Cordinate.fromString(s);
    }
}
