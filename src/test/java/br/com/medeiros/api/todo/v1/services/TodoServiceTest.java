package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.enums.Role;
import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Todo Service")
class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoService service;

    @Nested
    @DisplayName("When Create Todo")
    class CreateTodoTest{

        @Test
        @DisplayName("Should create a todo with ID and timestamps")
        void shouldCreateTodoWithIdAndTimestamps() {
            UserEntity user = new UserEntity("valid_user", "valid_pass", Role.USER);

            when(repository.save(any(TodoEntity.class)))
                    .thenAnswer(invocation -> {
                        TodoEntity todo = invocation.getArgument(0);
                        todo.setId(1L);
                        todo.setCreatedAt(LocalDateTime.now());
                        todo.setUpdatedAt(LocalDateTime.now());
                        return todo;
                    });

            TodoEntity created = service.createTodo(
                    new RequestCreateTodoDto("valid_title", "valid_description"),
                    user
            );

            assertAll("ID and timestamps",
                    () -> assertNotNull(created.getId(), "ID não deve ser nulo"),
                    () -> assertNotNull(created.getCreatedAt(), "createdAt não deve ser nulo"),
                    () -> assertNotNull(created.getUpdatedAt(), "updatedAt não deve ser nulo")
            );
        }

        @Test
        @DisplayName("Should populate todo fields from DTO and user")
        void shouldPopulateTodoFieldsCorrectly() {
            UserEntity user = new UserEntity("valid_user", "valid_pass", Role.USER);

            // Mock simples do repository
            when(repository.save(any(TodoEntity.class)))
                    .thenAnswer(invocation -> {
                        TodoEntity todo = invocation.getArgument(0);
                        todo.setId(1L);
                        return todo;
                    });

            TodoEntity created = service.createTodo(
                    new RequestCreateTodoDto("valid_title", "valid_description"),
                    user
            );

            assertAll("Fields validation",
                    () -> assertEquals("valid_title", created.getName(), "Nome incorreto"),
                    () -> assertEquals("valid_description", created.getDescription(), "Descrição incorreta"),
                    () -> assertEquals(TodoStatus.PENDING, created.getStatus(), "Status incorreto"),
                    () -> assertEquals(user, created.getUser(), "Usuário incorreto")
            );
        }

        @Test
        @DisplayName("Should propagate any exception from repository")
        void shouldPropagateAnyException() {
            UserEntity user = new UserEntity("valid_user", "valid_pass", Role.USER);

            when(repository.save(any())).thenThrow(new RuntimeException("Unexpected error"));

            assertThrows(Exception.class, () ->
                    service.createTodo(new RequestCreateTodoDto("title", "desc"), user)
            );
        }

        @Test
        @DisplayName("Should throw NullIdException when saved todo has null ID")
        void shouldThrowNullIdExceptionWhenIdIsNull() {
            UserEntity user = new UserEntity("valid_user", "valid_pass", Role.USER);

            when(repository.save(any(TodoEntity.class)))
                    .thenAnswer(invocation -> {
                        TodoEntity todo = invocation.getArgument(0);
                        return todo;
                    });

            assertThrows(NullIdException.class, () ->
                    service.createTodo(new RequestCreateTodoDto("title", "description"), user)
            );
        }

    }

}