package br.com.medeiros.api.todo.v1.data;

import jakarta.validation.constraints.NotEmpty;

public record RequestCreateTodoDto(
        @NotEmpty(message = "can not be empty")
        String name,
        String description
) {}
