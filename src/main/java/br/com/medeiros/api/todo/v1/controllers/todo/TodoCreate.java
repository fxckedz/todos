package br.com.medeiros.api.todo.v1.controllers.todo;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.services.TodoService;
import br.com.medeiros.api.todo.v1.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "Create", description = "Create Todos")
public class TodoCreate {

    private TodoService todoService;

    public TodoCreate(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML},
                 produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YAML})

    @Operation(
            summary = "Create a new Todo",
            description = "Create a new Todo by passing JSON, XML or YAML",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Todo successfully created", responseCode = "201", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
            })

    public ResponseEntity<ResponseDto> createTodo(
            @Valid @RequestBody RequestCreateTodoDto requestCreateTodoDto,
            @AuthenticationPrincipal UserEntity user) {

        var todo = todoService.createTodo(requestCreateTodoDto, user);

        var responseDto = ResponseDto.fromEntity(todo);

        responseDto.addAction("self",
                linkTo(methodOn(TodoCreate.class).createTodo(null, user)).toString(),
                "POST",
                "create a new TODO");

        responseDto.addAction("update",
                linkTo(methodOn(TodoUpdateById.class).updateTodoById(responseDto.id(), null, user)).toString(),
                "PUT",
                "update this TODO");

        responseDto.addAction("get",
                linkTo(methodOn(TodoFindById.class).findById(responseDto.id(), user)).toString(),
                "GET",
                "get a single TODO");

        responseDto.addAction("get all",
                linkTo(methodOn(TodoFindAll.class).findAll(user)).toString(),
                "GET",
                "get all TODOS");

        responseDto.addAction("delete",
                linkTo(methodOn(TodoDeleteById.class).deleteById(responseDto.id(), user)).toString(),
                "DELETE",
                "delete this TODO");



        return ResponseEntity.created(URI.create("/api/todos/v1/" + responseDto.id()))
                .body(responseDto);
    }
}
