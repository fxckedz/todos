package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

public class NullIdException extends RuntimeException {
    public NullIdException(){
        super("ID is null");
    }
}
