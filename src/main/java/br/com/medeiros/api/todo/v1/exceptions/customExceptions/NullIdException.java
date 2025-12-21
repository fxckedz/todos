package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

import org.springframework.http.HttpStatus;

public class NullIdException extends CustomException {
    private static final long serialVersionUID = 1L;

	public NullIdException(){super("ID is null", HttpStatus.BAD_REQUEST);}
}
