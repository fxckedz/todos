package br.com.medeiros.api.todo.v1.exceptions.handler;

import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
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
                "BAD REQUEST",
                details
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGenericExceptions(Exception ex){
        return new ExceptionResponse(
                new Date(),
                "INTERNAL SERVER ERROR",
                ex.getMessage()
        );
    }
}
