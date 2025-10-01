package br.com.medeiros.api.todo.v1.services;

import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import br.com.medeiros.api.todo.v1.entities.UserEntity;
import br.com.medeiros.api.todo.v1.enums.Role;
import br.com.medeiros.api.todo.v1.enums.TodoStatus;
import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NotFoundId;
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
import java.util.*;

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

            when(repository.save(any(TodoEntity.class))).thenAnswer(invocation -> {
                TodoEntity todo = invocation.getArgument(0);
                todo.setId(1L);
                return todo;
            });

            TodoEntity created = service.createTodo(validRequest, user);

            assertAll("Fields validation", () -> assertEquals("valid_title", created.getName(), "Nome deve vir do DTO"), () -> assertEquals("valid_description", created.getDescription(), "Descrição deve vir do DTO"), () -> assertEquals(TodoStatus.PENDING, created.getStatus(), "Status deve ser PENDING por padrão"), () -> assertEquals(user, created.getUser(), "User deve ser o passado como argumento"));

            verify(repository).save(any(TodoEntity.class));
        }

        @Test
        @DisplayName("Should set PENDING status by default")
        void shouldSetPendingStatusByDefault() {

            when(repository.save(any(TodoEntity.class))).thenAnswer(invocation -> {
                TodoEntity todo = invocation.getArgument(0);
                todo.setId(1L);
                return todo;
            });


            TodoEntity created = service.createTodo(validRequest, user);


            assertEquals(TodoStatus.PENDING, created.getStatus());
        }

        @Test
        @DisplayName("Should propagate repository exceptions")
        void shouldPropagateRepositoryExceptions() {

            when(repository.save(any(TodoEntity.class))).thenThrow(new RuntimeException("Database error"));


            assertThrows(RuntimeException.class, () -> service.createTodo(validRequest, user));

            verify(repository).save(any(TodoEntity.class));
        }

        @Test
        @DisplayName("Should throw NullIdException when repository returns todo without ID")
        void shouldThrowNullIdExceptionWhenIdIsNull() {

            when(repository.save(any(TodoEntity.class))).thenReturn(new TodoEntity());


            assertThrows(NullIdException.class, () -> service.createTodo(validRequest, user));

            verify(repository).save(any(TodoEntity.class));
        }

        @Test
        @DisplayName("Should call repository save with correct todo entity")
        void shouldCallRepositoryWithCorrectEntity() {

            TodoEntity savedTodo = new TodoEntity();
            savedTodo.setId(1L);

            when(repository.save(any(TodoEntity.class))).thenReturn(savedTodo);

            service.createTodo(validRequest, user);

            verify(repository).save(any(TodoEntity.class));
        }
    }

    @Nested
    @DisplayName("When Find All Todos")
    class FindAllTodoTest {

        @Test
        @DisplayName("Should return todo list")
        void ShouldReturnsTodoList() {

            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);
            List<TodoEntity> expectedTodos = Arrays.asList(new TodoEntity("todo_1", "Todo_1_desc", user), new TodoEntity("todo_2", "Todo_2_desc", user));

            when(repository.findByUser(user)).thenReturn(expectedTodos);

            List<TodoEntity> result = service.findAllTodos(user);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedTodos, result);
            verify(repository).findByUser(user);
        }

        @Test
        @DisplayName("Should return an empty list")
        void ShouldReturnsEmptyList() {

            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);
            when(repository.findByUser(user)).thenReturn(Collections.emptyList());

            List<TodoEntity> result = service.findAllTodos(user);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(repository).findByUser(user);
        }

        @Test
        @DisplayName("Should throw an exception if repository throws")
        void ShouldThrowsExceptionIfRepositoryThrows() {

            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);

            when(repository.findByUser(user)).thenThrow(new RuntimeException("Unexpected error"));

            assertThrows(RuntimeException.class, () -> {
                service.findAllTodos(user);
            });

            verify(repository).findByUser(user);
        }
    }

    @Nested
    @DisplayName("When A Todo By Id")
    class FindTodoByIdTest {

        @Test
        @DisplayName("Should Return NotFoundId if Invalid ID")
        void ShouldReturnNotFoundId(){

            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);

            when(repository.findById(any())).thenReturn(Optional.empty());

            assertThrows(NotFoundId.class, () -> {
                service.findTodoById(null, user);
            });
        }

        @Test
        @DisplayName("Should Return Todo When Todo Belongs To User")
        void shouldReturnTodoWhenTodoBelongsToUser() {

            Long todoId = 1L;
            Long userId = 1L;
            UserEntity user = new UserEntity(userId, "user_name", "user_pass", Role.USER);
            UserEntity todoOwner = new UserEntity(userId, "user_name", "user_pass", Role.USER);
            TodoEntity expectedTodo = new TodoEntity(todoId, "Test Todo", "test desc", todoOwner);

            when(repository.findById(todoId)).thenReturn(Optional.of(expectedTodo));

            TodoEntity result = service.findTodoById(todoId, user);

            assertEquals(expectedTodo, result);
            verify(repository).findById(todoId);
        }

        @Test
        @DisplayName("Should Throw NotFoundId When Todo Does Not Belong To User")
        void shouldThrowNotFoundIdWhenTodoDoesNotBelongToUser() {

            Long todoId = 1L;
            UserEntity currentUser = new UserEntity(1L, "current_user", "pass", Role.USER);
            UserEntity otherUser = new UserEntity(2L, "other_user", "pass", Role.USER);
            TodoEntity todoFromOtherUser = new TodoEntity(todoId, "Other Todo", "Other's Todo", otherUser);

            when(repository.findById(todoId)).thenReturn(Optional.of(todoFromOtherUser));

            assertThrows(NotFoundId.class, () -> {
                service.findTodoById(todoId, currentUser);
            });

            verify(repository).findById(todoId);
        }

        @Test
        @DisplayName("Should Propagate Repository Exception When FindById Fails")
        void shouldPropagateRepositoryExceptionWhenFindByIdFails() {

            Long todoId = 1L;
            UserEntity user = new UserEntity("user_name", "user_pass", Role.USER);
            String errorMessage = "Database connection failed";

            when(repository.findById(todoId)).thenThrow(new RuntimeException(errorMessage));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                service.findTodoById(todoId, user);
            });

            assertEquals(errorMessage, exception.getMessage());
            verify(repository).findById(todoId);
        }
    }
}