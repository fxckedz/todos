package br.com.medeiros.api.todo.v1.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
){
   
        ExceptionResponse body = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getBindingResult().getFieldError().getDefaultMessage(),
                request.getRequestURI());
        

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body); 
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericExceptions(
        Exception ex,
        HttpServletRequest request){

        ExceptionResponse body = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }   

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(
        CustomException ex,
        HttpServletRequest request
) {
        ExceptionResponse body = new ExceptionResponse(
                ex.getStatus().value(),
                ex.getError(),
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(ex.getStatus().value()).body(body);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(
        UsernameNotFoundException ex,
        HttpServletRequest request
) {
        
        ExceptionResponse body = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
