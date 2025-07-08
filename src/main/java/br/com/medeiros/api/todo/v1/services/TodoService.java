package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.RequestDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
import org.springframework.stereotype.Service;

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

        return savedTodo.getId();
    }

}
