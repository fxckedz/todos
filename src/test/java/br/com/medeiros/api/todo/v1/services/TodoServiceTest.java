package br.com.medeiros.api.todo.v1.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Test
    void createTodo_ShouldReturnSavedTodoId() {
        String name = "valid_name";
        String description = "valid_description";
        TodoEntity savedTodo = new TodoEntity(name, description);
        UUID expectedId = UUID.randomUUID();
        savedTodo.setId(expectedId);

        when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedTodo);

        UUID id = todoService.createTodo(name, description);

        assertEquals(expectedId, id);

        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void createTodo_ShouldThrowException_WhenIdNull() {
        String name = "valid_name";
        String description = "valid_description";
        TodoEntity savedTodo = new TodoEntity(name, description);
        savedTodo.setId(null);

        when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedTodo);

        assertThrows(NullIdException.class, () -> {
            todoService.createTodo(name, description);
        });

        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void createTodo_ShouldCreateEntityWithCorrectDataAndNonNullId() {
        // Arrange
        String name = "valid_name";
        String description = "valid_description";
        TodoStatus expectedStatus = TodoStatus.PENDING;
        UUID expectedId = UUID.randomUUID();
            
        when(todoRepository.save(any(TodoEntity.class)))
            .thenAnswer(invocation -> {
                TodoEntity entity = invocation.getArgument(0);
                // Simula o JPA definindo o ID e timestamps
                entity.setId(expectedId);
                LocalDateTime now = LocalDateTime.now();
                entity.setCreatedAt(now);
                entity.setUpdatedAt(now);
                return entity;
            });

        UUID returnedId = todoService.createTodo(name, description);

        ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
        verify(todoRepository).save(captor.capture());
            
        TodoEntity savedEntity = captor.getValue();
            
        assertEquals(name, savedEntity.getName());
        assertEquals(description, savedEntity.getDescription());
        assertEquals(expectedStatus, savedEntity.getStatus());
            
        assertNotNull(savedEntity.getId());
        assertEquals(expectedId, savedEntity.getId());
        assertEquals(expectedId, returnedId);
            
        assertNotNull(savedEntity.getCreatedAt());
        assertNotNull(savedEntity.getUpdatedAt());
        }
}

