package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {

    public UserAlreadyExistsException() {super("User Already Exists", HttpStatus.CONFLICT, "Conflict");}

}
