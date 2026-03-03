package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends CustomException {

    public InvalidCredentialsException(String message) {super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");}

}
