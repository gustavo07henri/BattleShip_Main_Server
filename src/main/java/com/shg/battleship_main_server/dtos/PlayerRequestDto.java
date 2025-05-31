package com.shg.battleship_main_server.dtos;

import jakarta.validation.constraints.Email;

public record PlayerRequestDto(
        String name,
        @Email(message = "Email deve ser válido") String email) {
}
