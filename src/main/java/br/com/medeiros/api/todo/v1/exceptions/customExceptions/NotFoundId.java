package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

import org.springframework.http.HttpStatus;

public class NotFoundId extends CustomException{
    private static final long serialVersionUID = 1L;

	public NotFoundId() {super("ID does not exists", HttpStatus.BAD_REQUEST);}
}
