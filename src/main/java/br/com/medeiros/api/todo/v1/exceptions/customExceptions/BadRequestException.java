package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {
    private static final long serialVersionUID = 1L;

	public BadRequestException(String message){super(message, HttpStatus.BAD_REQUEST, "Bad Request");}
}
