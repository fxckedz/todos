package br.com.medeiros.api.todo.v1.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.*;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
import br.com.medeiros.api.todo.v1.repositories.TodoRepository;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Nested
    class CreateTodo {
        private final RequestCreateTodoDto validRequest =
                new RequestCreateTodoDto("valid_name", "valid_description");

        @Test
        void shouldReturnSavedTodoId() {
            // Arrange
            TodoEntity savedTodo = new TodoEntity(validRequest.name(), validRequest.description());
            UUID expectedId = UUID.randomUUID();
            savedTodo.setId(expectedId);

            when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedTodo);

            // Act
            UUID id = todoService.createTodo(validRequest);

            // Assert
            assertEquals(expectedId, id);
            verify(todoRepository, times(1)).save(any(TodoEntity.class));
        }

        @Test
        void shouldThrowException_WhenIdNull() {
            // Arrange
            TodoEntity savedTodo = new TodoEntity(validRequest.name(), validRequest.description());
            savedTodo.setId(null);

            when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedTodo);

            // Act & Assert
            assertThrows(NullIdException.class, () -> {
                todoService.createTodo(validRequest);
            });

            verify(todoRepository, times(1)).save(any(TodoEntity.class));
        }

        @Test
        void shouldCreateEntityWithCorrectDataAndNonNullId() {
            // Arrange
            TodoStatus expectedStatus = TodoStatus.PENDING;
            UUID expectedId = UUID.randomUUID();

            when(todoRepository.save(any(TodoEntity.class)))
                    .thenAnswer(invocation -> {
                        TodoEntity entity = invocation.getArgument(0);
                        entity.setId(expectedId);
                        LocalDateTime now = LocalDateTime.now();
                        entity.setCreatedAt(now);
                        entity.setUpdatedAt(now);
                        return entity;
                    });

            // Act
            UUID returnedId = todoService.createTodo(validRequest);

            // Assert
            ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
            verify(todoRepository).save(captor.capture());

            TodoEntity savedEntity = captor.getValue();

            assertEquals(validRequest.name(), savedEntity.getName());
            assertEquals(validRequest.description(), savedEntity.getDescription());
            assertEquals(expectedStatus, savedEntity.getStatus());

            assertNotNull(savedEntity.getId());
            assertEquals(expectedId, savedEntity.getId());
            assertEquals(expectedId, returnedId);

            assertNotNull(savedEntity.getCreatedAt());
            assertNotNull(savedEntity.getUpdatedAt());
        }
    }

    @Nested
    class FindAllTodos{

        @Test
        void ShouldReturnListOfTodosIfExists(){
            List<TodoEntity> mockTodos = Arrays.asList(
                    new TodoEntity("Tarefa 1", "Descrição 1"),
                    new TodoEntity("Tarefa 2", "Descrição 2")
            );

            when(todoRepository.findAll()).thenReturn(mockTodos);

            List<TodoEntity> result = todoService.findAllTodos();

            assertEquals(2, result.size());
            verify(todoRepository, times(1)).findAll();
        }

        @Test
        void ShouldReturnEmptyListIfTodoNotExists(){

            when(todoRepository.findAll()).thenReturn(Collections.emptyList());

            List<TodoEntity> result = todoService.findAllTodos();

            assertTrue(result.isEmpty());
            verify(todoRepository, times(1)).findAll();
        }

        @Test
        void ShouldThrowsExceptionWhenRepositoryThrows(){
            when(todoRepository.findAll()).thenThrow(new RuntimeException("Erro no banco de dados"));
            assertThrows(RuntimeException.class, () -> todoService.findAllTodos());
            verify(todoRepository, times(1)).findAll();
        }

    }
}

