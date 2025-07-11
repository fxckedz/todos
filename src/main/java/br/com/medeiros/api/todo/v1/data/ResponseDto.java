package br.com.medeiros.api.todo.v1.data;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.enums.TodoStatus;

public record ResponseDto(
    UUID id,
    String name,
    String description,
    TodoStatus status,
    LocalDateTime createdAt
) {

    public static ResponseDto fromEntity(TodoEntity todoEntity){
        return new ResponseDto(
            todoEntity.getId(),
            todoEntity.getName(),
            todoEntity.getDescription(),
            todoEntity.getStatus(), 
            todoEntity.getCreatedAt());
    }
}

