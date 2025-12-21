package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.data.RequestUpdateTodoByIdDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NotFoundId;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoEntity createTodo(RequestCreateTodoDto req, UserEntity user){
        var todo = new TodoEntity(req.name(), req.description(), user);
        var savedTodo = todoRepository.save(todo);

        if(savedTodo.getId() == null){
            throw new NullIdException();
        }

        return savedTodo;
    }

    public List<TodoEntity> findAllTodos(UserEntity user){
        return todoRepository.findByUser(user);
    }

    public TodoEntity findTodoById(Long id, UserEntity user){
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(NotFoundId::new);

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new NotFoundId();
        }

        return todo;
    }

    public void deleteTodoById(Long id, UserEntity user) {
        TodoEntity todo = findTodoById(id, user);
        todoRepository.delete(todo);
    }

    public TodoEntity updateTodoById(Long id, RequestUpdateTodoByIdDto req, UserEntity user){
        TodoEntity todo = findTodoById(id, user);

        if (req.name() != null) todo.setName(req.name());
        if (req.description() != null) todo.setDescription(req.description());
        if (req.status() != null) todo.setStatus(req.status());

        return todoRepository.save(todo);
    }

}
