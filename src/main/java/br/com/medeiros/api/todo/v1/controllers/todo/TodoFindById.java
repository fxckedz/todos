package br.com.medeiros.api.todo.v1.controllers.todo;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "FindById", description = "Find Todo By Id")
public class TodoFindById {

    private TodoService todoService;

    public TodoFindById(TodoService todoService) {
        this.todoService = todoService;
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
    public ResponseEntity<ResponseDto> findById(
            @PathVariable String stringId,
            @AuthenticationPrincipal UserEntity user) {

        UUID id = UUID.fromString(stringId);

        var entity = todoService.findTodoById(id, user);

        System.out.println("USER = " + user);

        ResponseDto responseDto = ResponseDto.fromEntity(entity);

        responseDto.add(linkTo(methodOn(TodoFindById.class)
                .findById(stringId, user))
                .withSelfRel());

        return ResponseEntity.ok(responseDto);
    }

}
