package br.com.medeiros.api.todo.v1.data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "status",
        "createdAt",
        "links"
})

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ResponseDto extends RepresentationModel<ResponseDto> {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("status")
    private final TodoStatus status;

    @JsonProperty("createdAt")
    private final LocalDateTime createdAt;

    public ResponseDto(UUID id, String name, String description, TodoStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public TodoStatus status() {
        return status;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ResponseDto) obj;
        return this.id.equals(that.id) &&
                this.name.equals(that.name) &&
                this.description.equals(that.description) &&
                this.status.equals(that.status) &&
                this.createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, createdAt);
    }

    @Override
    public String toString() {
        return "ResponseDto[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "status=" + status + ", " +
                "createdAt=" + createdAt + ']';
    }

    public static ResponseDto fromEntity(TodoEntity todoEntity) {
        return new ResponseDto(
                todoEntity.getId(),
                todoEntity.getName(),
                todoEntity.getDescription(),
                todoEntity.getStatus(),
                todoEntity.getCreatedAt());
    }
}