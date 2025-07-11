package br.com.medeiros.api.todo.v1.exceptions.customExceptions;

public class NotFoundId extends RuntimeException{
    public  NotFoundId(){super("Id does not exists");}
}
