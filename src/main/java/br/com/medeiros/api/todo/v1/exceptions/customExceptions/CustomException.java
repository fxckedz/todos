package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;
	private final HttpStatus status;
    private final String error;

    public CustomException(String message, HttpStatus status, String error){
        super(message);
        this.status = status;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError(){
        return error;
    }
}
