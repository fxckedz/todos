package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.data.RequestUpdateTodoByIdDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NotFoundId;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoEntity createTodo(RequestCreateTodoDto req){
        var todo = new TodoEntity(req.name(), req.description());
        var savedTodo = todoRepository.save(todo);

        if(savedTodo.getId() == null){
            throw new NullIdException();
        }

        return savedTodo;
    }

    public List<TodoEntity> findAllTodos(){
        return todoRepository.findAll();
    }

    public TodoEntity findTodoById(UUID id){
        Optional<TodoEntity> entity = todoRepository.findById(id);

        if(entity.isEmpty()){
            throw new NotFoundId();
        }

        return entity.get();
    }

    public void deleteTodoById(UUID id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        }
    }

    public TodoEntity updateTodoById(UUID id, RequestUpdateTodoByIdDto req){
        Optional<TodoEntity> entity = todoRepository.findById(id);

        if(entity.isEmpty()){
            throw new NotFoundId();
        }

        var en = entity.get();

        if (req.name() != null) {
            en.setName(req.name());
        }
        if (req.description() != null) {
            en.setDescription(req.description());
        }

        if (req.status() != null) {
            en.setStatus(req.status());
        }

        return todoRepository.save(en);
    }

}
