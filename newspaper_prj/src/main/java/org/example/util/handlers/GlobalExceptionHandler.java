package org.example.util.handlers;

import org.example.util.errorResponses.ErrorResponse;
import org.example.util.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException userNotFoundException){
        ErrorResponse response = new ErrorResponse(
                "User with this id wasn't found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserIncorrectException userIncorrectException){
        ErrorResponse response = new ErrorResponse(
                userIncorrectException.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(PostNotFoundException postNotFoundException){
        ErrorResponse response = new ErrorResponse(
                "Post with this id wasn't found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(PostNotCreatedException postNotCreatedException){
        ErrorResponse response = new ErrorResponse(
                postNotCreatedException.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AuthFormIncorrectException authFormNotCorrectException){
        ErrorResponse response = new ErrorResponse(
                authFormNotCorrectException.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException accessDeniedException){
        ErrorResponse response = new ErrorResponse(
                "Неправильный jwt токен или не пройдена аутентифика",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
