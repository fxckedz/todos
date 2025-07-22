package br.com.medeiros.api.todo.v1.controllers;

import br.com.medeiros.api.todo.v1.data.RequestUpdateTodoByIdDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.services.TodoService;
import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "Todos", description = "Endpoints to Managing Todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
    @Operation(summary = "Create a new Todo",
            description = "Create a new Todo by passing JSON, XML or YAML",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            })
    public ResponseEntity<ResponseDto> createTodo(@Valid @RequestBody RequestCreateTodoDto requestCreateTodoDto) {
        var todo = todoService.createTodo(requestCreateTodoDto);

        var responseDto = ResponseDto.fromEntity(todo);

        return ResponseEntity.created(URI.create("/api/todos/v1/" + responseDto.id())).body(responseDto);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})
    @Operation(summary = "Find all todos",
            description = "Find all todos",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class))
                            )
                    }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<ResponseDto>> findAllTodos() {

        var todoEntities = todoService.findAllTodos();

        if (todoEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var todos = todoEntities.stream().map(ResponseDto::fromEntity).toList();

        todos.forEach(p -> p.add(linkTo(methodOn(TodoController.class).findTodoById(p.id().toString())).withSelfRel()));

        return ResponseEntity.ok(todos);
    }

    @GetMapping(value = "/{stringId}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
    )
    @Operation(summary = "Find todo by id",
            description = "Find todo by id",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            })
    public ResponseEntity<ResponseDto> findTodoById(@PathVariable String stringId) {

        UUID id = UUID.fromString(stringId);

        var entity = todoService.findTodoById(id);

        ResponseDto responseDto = new ResponseDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt());

        responseDto.add(linkTo(methodOn(TodoController.class).findTodoById(stringId)).withSelfRel());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{stringId}")
    @Operation(summary = "Delete todo by id",
            description = "Delete todo by id",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public ResponseEntity<Void> deleteTodoById(@PathVariable String stringId) {

        UUID id = UUID.fromString(stringId);

        todoService.deleteTodoById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{stringId}",
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML}
    )
    @Operation(summary = "Update todo by id",
            description = "update todo by id",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            })
    public ResponseEntity<ResponseDto> updateTodoById(
            @PathVariable String stringId,
            @RequestBody RequestUpdateTodoByIdDto req) {

        UUID id = UUID.fromString(stringId);

        TodoEntity todo = todoService.updateTodoById(id, req);

        ResponseDto responseDto = ResponseDto.fromEntity(todo);

        responseDto.add(linkTo(methodOn(TodoController.class).findTodoById(stringId)).withSelfRel());

        return ResponseEntity.ok(responseDto);
    }

}
