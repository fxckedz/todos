package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.enums.Role;
import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Todo Service")
class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoService service;

    @Nested
    @DisplayName("When Create Todo")
    class CreateTodoTest {

        private UserEntity user;
        private RequestCreateTodoDto validRequest;

        @BeforeEach
        void setUp() {
            user = new UserEntity("valid_user", "valid_pass", Role.USER);
            validRequest = new RequestCreateTodoDto("valid_title", "valid_description");
        }

        @Test
        @DisplayName("Should create todo with correct fields from DTO and user")
        void shouldCreateTodoWithCorrectFields() {
            // Arrange
            when(repository.save(any(TodoEntity.class))).thenAnswer(invocation -> {
                TodoEntity todo = invocation.getArgument(0);
                todo.setId(1L); // Apenas seta o ID para o teste passar
                return todo;
            });

            // Act
            TodoEntity created = service.createTodo(validRequest, user);

            // Assert - Foca apenas nos campos que o SERVICE é responsável
            assertAll("Fields validation", () -> assertEquals("valid_title", created.getName(), "Nome deve vir do DTO"), () -> assertEquals("valid_description", created.getDescription(), "Descrição deve vir do DTO"), () -> assertEquals(TodoStatus.PENDING, created.getStatus(), "Status deve ser PENDING por padrão"), () -> assertEquals(user, created.getUser(), "User deve ser o passado como argumento"));

            verify(repository).save(any(TodoEntity.class));
        }

        @Test
        @DisplayName("Should set PENDING status by default")
        void shouldSetPendingStatusByDefault() {
            // Arrange
            when(repository.save(any(TodoEntity.class))).thenAnswer(invocation -> {
                TodoEntity todo = invocation.getArgument(0);
                todo.setId(1L);
                return todo;
            });

            // Act
            TodoEntity created = service.createTodo(validRequest, user);

            // Assert - Teste específico para o comportamento padrão do status
            assertEquals(TodoStatus.PENDING, created.getStatus());
        }

        @Test
        @DisplayName("Should propagate repository exceptions")
        void shouldPropagateRepositoryExceptions() {
            // Arrange
            when(repository.save(any(TodoEntity.class))).thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> service.createTodo(validRequest, user));

            verify(repository).save(any(TodoEntity.class));
        }

        @Test
        @DisplayName("Should throw NullIdException when repository returns todo without ID")
        void shouldThrowNullIdExceptionWhenIdIsNull() {
            // Arrange - Repository retorna todo sem ID (simulando falha no save)
            when(repository.save(any(TodoEntity.class))).thenReturn(new TodoEntity()); // Retorna objeto sem ID

            // Act & Assert
            assertThrows(NullIdException.class, () -> service.createTodo(validRequest, user));

            verify(repository).save(any(TodoEntity.class));
        }

        @Test
        @DisplayName("Should call repository save with correct todo entity")
        void shouldCallRepositoryWithCorrectEntity() {
            // Arrange
            TodoEntity savedTodo = new TodoEntity();
            savedTodo.setId(1L);

            when(repository.save(any(TodoEntity.class))).thenReturn(savedTodo);

            // Act
            service.createTodo(validRequest, user);

            // Assert - Verifica que o repository foi chamado com uma TodoEntity
            verify(repository).save(any(TodoEntity.class));
        }
    }

    @Nested
    @DisplayName("When Find All Todos")
    class FindAllTodoTest {

        @Test
        @DisplayName("Should return todo list")
        void ShouldReturnsTodoList() {
            // Arrange
            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);
            List<TodoEntity> expectedTodos = Arrays.asList(new TodoEntity("todo_1", "Todo_1_desc", user), new TodoEntity("todo_2", "Todo_2_desc", user));

            when(repository.findByUser(user)).thenReturn(expectedTodos);

            // Act
            List<TodoEntity> result = service.findAllTodos(user);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedTodos, result);
            verify(repository).findByUser(user);
        }

        @Test
        @DisplayName("Should return an empty list")
        void ShouldReturnsEmptyList() {
            // Arrange
            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);
            when(repository.findByUser(user)).thenReturn(Collections.emptyList());

            // Act
            List<TodoEntity> result = service.findAllTodos(user);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(repository).findByUser(user);
        }

        @Test
        @DisplayName("Should throw an exception if repository throws")
        void ShouldThrowsExceptionIfRepositoryThrows() {
            // Arrange
            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);

            when(repository.findByUser(user)).thenThrow(new RuntimeException("Unexpected error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                service.findAllTodos(user);
            });

            verify(repository).findByUser(user);
        }
    }
}