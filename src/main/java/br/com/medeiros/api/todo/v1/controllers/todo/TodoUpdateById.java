package br.com.medeiros.api.todo.v1.controllers.todo;

import br.com.medeiros.api.todo.v1.data.RequestUpdateTodoByIdDto;
import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.exceptions.ExceptionResponse;
import br.com.medeiros.api.todo.v1.services.TodoService;
import br.com.medeiros.api.todo.v1.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "UpdateById", description = "Update todo by Id")
public class TodoUpdateById {

    private TodoService todoService;

    public TodoUpdateById(TodoService todoService) {
        this.todoService = todoService;
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
            @RequestBody RequestUpdateTodoByIdDto req,
            @AuthenticationPrincipal UserEntity user) {

        UUID id = UUID.fromString(stringId);

        TodoEntity todo = todoService.updateTodoById(id, req, user);

        ResponseDto responseDto = ResponseDto.fromEntity(todo);

        responseDto.add(linkTo(methodOn(TodoFindById.class)
                .findById(stringId, user))
                .withSelfRel());

        return ResponseEntity.ok(responseDto);
    }

}
