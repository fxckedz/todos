package br.com.medeiros.api.todo.v1.controllers.todo;

import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.services.TodoService;
import br.com.medeiros.api.todo.v1.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "FindAll", description = "Find all todos")
public class TodoFindAll {

    private TodoService todoService;

    public TodoFindAll(TodoService todoService) {
        this.todoService = todoService;
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

    public ResponseEntity<List<ResponseDto>> findAll(@AuthenticationPrincipal UserEntity user) {

        var todoEntities = todoService.findAllTodos(user);

        if (todoEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var todos = todoEntities.stream()
                .map(ResponseDto::fromEntity)
                .toList();

        todos.forEach(p -> p.add(linkTo(methodOn(TodoFindAll.class)
                .findAll(user))
                .withSelfRel()));

        return ResponseEntity.ok(todos);
    }

}
