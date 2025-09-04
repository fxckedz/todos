package br.com.medeiros.api.todo.v1.controllers.todo;

import br.com.medeiros.api.todo.v1.data.ResponseDto;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.services.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "DeleteById", description = "Delete Todos by id")
public class TodoDeleteById {

    private TodoService todoService;

    public TodoDeleteById(TodoService todoService) {
        this.todoService = todoService;
    }

    @DeleteMapping("/{stringId}")

    @Operation(summary = "Delete todo by id",
            description = "Delete todo by id",
            tags = {"Todos"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })

    public ResponseEntity<Void> deleteById(
            @PathVariable String stringId,
            @AuthenticationPrincipal UserEntity user) {

        UUID id = UUID.fromString(stringId);

        todoService.deleteTodoById(id, user);

        return ResponseEntity.ok().build();
    }

}
