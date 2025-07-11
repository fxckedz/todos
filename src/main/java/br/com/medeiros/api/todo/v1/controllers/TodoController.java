package br.com.medeiros.api.todo.v1.controllers;

import br.com.medeiros.api.todo.v1.services.TodoService;
import br.com.medeiros.api.todo.v1.data.RequestDto;
import br.com.medeiros.api.todo.v1.data.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos/v1")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(@Valid @RequestBody RequestDto requestDto){
        var id = todoService.createTodo(requestDto.name(), requestDto.description());
        return ResponseEntity.created(URI.create("/api/todos/v1/" + id.toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseDto>> findAllTodos(){

        var todoEntities = todoService.findAllTodos();

        if(todoEntities.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        var todos = todoEntities.stream().map(ResponseDto::fromEntity).toList();

        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{stringId}")
    public ResponseEntity<ResponseDto> findTodoById(@PathVariable String stringId){

        UUID id = UUID.fromString(stringId);

        var entity =  todoService.findTodoById(id);

        ResponseDto responseDto = new ResponseDto(entity.getName(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{stringId}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable String stringId){

        UUID id = UUID.fromString(stringId);

        todoService.deleteTodoById(id);

        return ResponseEntity.ok().build();
    }

}
