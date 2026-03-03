package br.com.medeiros.api.todo.v1.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import jakarta.validation.constraints.Size;

public record RequestUpdateTodoByIdDto(

        @Size(min = 1)
        String name,
        String description,
        @JsonProperty("status") TodoStatus status
) {
}
