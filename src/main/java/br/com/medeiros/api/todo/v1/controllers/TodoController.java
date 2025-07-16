package br.com.medeiros.api.todo.v1.controllers;

import br.com.medeiros.api.todo.v1.data.RequestUpdateTodoByIdDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.services.TodoService;
import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.util.MediaType;
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

    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
                 produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
                )
    public ResponseEntity<Void> createTodo(@Valid @RequestBody RequestCreateTodoDto requestCreateTodoDto){
        var id = todoService.createTodo(requestCreateTodoDto);
        return ResponseEntity.created(URI.create("/api/todos/v1/" + id.toString())).build();
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
    public ResponseEntity<List<ResponseDto>> findAllTodos(){

        var todoEntities = todoService.findAllTodos();

        if(todoEntities.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        var todos = todoEntities.stream().map(ResponseDto::fromEntity).toList();

        return ResponseEntity.ok(todos);
    }

    @GetMapping(value = "/{stringId}",
                produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
                )
    public ResponseEntity<ResponseDto> findTodoById(@PathVariable String stringId){

        UUID id = UUID.fromString(stringId);

        var entity =  todoService.findTodoById(id);

        ResponseDto responseDto = new ResponseDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{stringId}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable String stringId){

        UUID id = UUID.fromString(stringId);

        todoService.deleteTodoById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{stringId}",
                consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
                produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
                )
    public ResponseEntity<ResponseDto> updateTodoById(
            @PathVariable String stringId,
            @RequestBody RequestUpdateTodoByIdDto req){

        UUID id = UUID.fromString(stringId);

        TodoEntity entity = todoService.updateTodoById(id, req);

        ResponseDto responseDto = new ResponseDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt());

        return ResponseEntity.ok(responseDto);
    }

}
