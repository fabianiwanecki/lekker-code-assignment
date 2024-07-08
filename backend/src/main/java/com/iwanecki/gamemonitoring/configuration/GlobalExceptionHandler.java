package com.iwanecki.gamemonitoring.configuration;

import com.iwanecki.gamemonitoring.team.*;
import com.iwanecki.gamemonitoring.user.AlreadyTeamMemberException;
import com.iwanecki.gamemonitoring.user.UserAlreadyExistsException;
import com.iwanecki.gamemonitoring.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto genericExceptionHandler(Exception e) {
        e.printStackTrace();
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto authorizationDeniedExceptionHandler(AuthorizationDeniedException e) {
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            messages.add(fieldName + " " + errorMessage);
        });
        return new ErrorDto(String.join("; ", messages));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto userAlreadyExistsExceptionHandler(UserAlreadyExistsException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto userNotFoundExceptionHandler(UserNotFoundException e) {
        return new ErrorDto(e.getMessage());
    }


    @ExceptionHandler(TeamNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto teamNotFoundExceptionHandler(TeamNotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(TeamUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto teamUpdateExceptionHandler(TeamUpdateException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(TeamRequestIsPendingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto teamRequestIsPendingExceptionHandler(TeamRequestIsPendingException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(TeamRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto teamRequestNotFoundExceptionHandler(TeamRequestNotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(AlreadyTeamMemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto alreadyTeamMemberExceptionHandler(AlreadyTeamMemberException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(TeamAlreadyFullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto teamAlreadyFullExceptionHandler(TeamAlreadyFullException e) {
        return new ErrorDto(e.getMessage());
    }

}
