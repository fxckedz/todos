package br.com.medeiros.api.todo.v1.exceptions.handler;

import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.CustomException;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NotFoundId;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleValidationExceptions(MethodArgumentNotValidException ex){
        String details = ex.getBindingResult().
                getFieldErrors().
                stream().
                map(error -> error.getField() + ": " + error.getDefaultMessage()).
                collect(Collectors.joining(", "));

        return new ExceptionResponse(
                new Date(),
                HttpStatus.BAD_REQUEST,
                details
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGenericExceptions(Exception ex){
        return new ExceptionResponse(
                new Date(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(CustomException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ExceptionResponse(
                        new Date(),
                        ex.getStatus(),
                        ex.getMessage()
                ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(
                        new Date(),
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                ));
    }
}
