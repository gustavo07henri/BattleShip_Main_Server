package com.shg.battleship_main_server.controllers;

import com.shg.battleship_main_server.dtos.ErrorMessage;
import com.shg.battleship_main_server.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {



    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> PlayerNotAvailable(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> PlayerStateInGame(IllegalStateException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> PlayerBeDifferent(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAllreadyInUse.class)
    public ResponseEntity<String> emailAllreadyInUseHandle(EmailAllreadyInUse ex){
        return ResponseEntity.status(HttpStatus.IM_USED).body(ex.getMessage());
    }

    @ExceptionHandler(PlayerAlreadyInGameException.class)
    public ResponseEntity<String> PlayerAlreadyInGameHandle(PlayerAlreadyInGameException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     *
     * @param ex
     * @return ErrorMessage in case of requisition bad success
     */
    @MessageExceptionHandler(GameNotFoundException.class)
    @SendToUser("/queue/errors")
    public ErrorMessage handleGameNotFound(GameNotFoundException ex) {
        return new ErrorMessage(ex.getClass().getSimpleName(),ex.getMessage(), "GAME_NOT_FOUND");
    }

    @MessageExceptionHandler(UserNotFoundException.class)
    @SendToUser("/queue/errors")
    public ErrorMessage handleUserNotFound(UserNotFoundException ex) {
        return new ErrorMessage(ex.getClass().getSimpleName(),ex.getMessage(), "USER_NOT_FOUND");
    }

    @MessageExceptionHandler({
            GameNotInProgressException.class,
            NotPlayerTurnException.class,
            SelfAttackNotAllowedException.class,
            CoordinateAlreadyAttackedException.class
    })
    @SendToUser("/queue/errors")
    public ErrorMessage handleBadRequest(RuntimeException ex) {
        return new ErrorMessage(ex.getClass().getSimpleName(),ex.getMessage(), "INVALID_REQUEST");
    }

    @MessageExceptionHandler(RecoverySessionException.class)
    @SendToUser("/queue/errors")
    public ErrorMessage handleGRecoverySession(RecoverySessionException ex) {
        return new ErrorMessage(ex.getClass().getSimpleName(),ex.getMessage(), "NOT_FOUND_OR_EXPIRED");
    }



}
