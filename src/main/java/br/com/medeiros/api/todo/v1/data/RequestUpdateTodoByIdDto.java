package br.com.medeiros.api.todo.v1.data;

import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestUpdateTodoByIdDto(
        String name,
        String description,
        @JsonProperty("status") TodoStatus status
) {
}
