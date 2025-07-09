package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public UUID createTodo(String name, String description){
        var todo = new TodoEntity(name, description);
        var savedTodo = todoRepository.save(todo);

        if(savedTodo.getId() == null){
            throw new NullIdException();
        }

        return savedTodo.getId();
    }

    public List<ResponseDto> findAllTodos(){
        return todoRepository.
            findAll().
            stream().
            map(ResponseDto::fromEntity)
            .toList();
    }

}
