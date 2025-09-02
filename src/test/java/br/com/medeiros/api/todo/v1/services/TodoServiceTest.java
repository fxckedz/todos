//package br.com.medeiros.api.todo.v1.services;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import br.com.medeiros.api.todo.v1.data.RequestCreateTodoDto;
//import br.com.medeiros.api.todo.v1.data.RequestUpdateTodoByIdDto;
//import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NotFoundId;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import br.com.medeiros.api.todo.v1.entities.TodoEntity;
//import br.com.medeiros.api.todo.v1.enums.TodoStatus;
//import br.com.medeiros.api.todo.v1.exceptions.customExceptions.NullIdException;
//import br.com.medeiros.api.todo.v1.repositories.TodoRepository;
//
//
//@ExtendWith(MockitoExtension.class)
//public class TodoServiceTest {
//
//    @Mock
//    private TodoRepository todoRepository;
//
//    @InjectMocks
//    private TodoService todoService;
//
//    @Nested
//    class CreateTodo {
//        private final RequestCreateTodoDto validRequest =
//                new RequestCreateTodoDto("valid_name", "valid_description");
//
//        @Test
//        void shouldReturnSavedTodo() {
//
//            UUID id = UUID.randomUUID();
//            TodoEntity savedTodo = new TodoEntity(validRequest.name(), validRequest.description());
//            savedTodo.setId(id);
//
//
//            when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedTodo);
//
//            TodoEntity todo = todoService.createTodo(validRequest);
//
//            assertEquals(todo, savedTodo);
//            assertEquals(id, todo.getId()); // Verifica se o ID está correto
//            verify(todoRepository, times(1)).save(any(TodoEntity.class));
//        }
//
//        @Test
//        void shouldThrowException_WhenIdNull() {
//
//            TodoEntity savedTodo = new TodoEntity(validRequest.name(), validRequest.description());
//            savedTodo.setId(null);
//
//            when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedTodo);
//
//
//            assertThrows(NullIdException.class, () -> {
//                todoService.createTodo(validRequest);
//            });
//
//            verify(todoRepository, times(1)).save(any(TodoEntity.class));
//        }
//
//        @Test
//        void shouldCreateEntityWithCorrectDataAndNonNullId() {
//
//            TodoStatus expectedStatus = TodoStatus.PENDING;
//            UUID expectedId = UUID.randomUUID();
//
//            when(todoRepository.save(any(TodoEntity.class)))
//                    .thenAnswer(invocation -> {
//                        TodoEntity entity = invocation.getArgument(0);
//                        entity.setId(expectedId);
//                        LocalDateTime now = LocalDateTime.now();
//                        entity.setCreatedAt(now);
//                        entity.setUpdatedAt(now);
//                        return entity;
//                    });
//
//
//            TodoEntity todo = todoService.createTodo(validRequest);
//
//            ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
//            verify(todoRepository).save(captor.capture());
//
//            TodoEntity savedEntity = captor.getValue();
//
//            assertEquals(validRequest.name(), savedEntity.getName());
//            assertEquals(validRequest.description(), savedEntity.getDescription());
//            assertEquals(expectedStatus, savedEntity.getStatus());
//
//            assertNotNull(savedEntity.getId());
//            assertEquals(expectedId, savedEntity.getId());
//            assertEquals(expectedId, savedEntity.getId());
//
//            assertNotNull(savedEntity.getCreatedAt());
//            assertNotNull(savedEntity.getUpdatedAt());
//        }
//
//        @Test
//        void ShouldThrowsExceptionWhenRepositoryThrows(){
//            TodoEntity todoEntity = new TodoEntity(validRequest.name(), validRequest.description());
//
//            when(todoRepository.save(todoEntity))
//                    .thenThrow(new RuntimeException("Erro no banco de dados"));
//
//            assertThrows(RuntimeException.class, () -> todoService.createTodo(validRequest));
//            verify(todoRepository, times(1)).save(any(TodoEntity.class));
//        }
//    }
//
//    @Nested
//    class FindAllTodos{
//
//        @Test
//        void ShouldReturnListOfTodosIfExists(){
//            List<TodoEntity> mockTodos = Arrays.asList(
//                    new TodoEntity("Tarefa 1", "Descrição 1"),
//                    new TodoEntity("Tarefa 2", "Descrição 2")
//            );
//
//            when(todoRepository.findAll()).thenReturn(mockTodos);
//
//            List<TodoEntity> result = todoService.findAllTodos();
//
//            assertEquals(2, result.size());
//            verify(todoRepository, times(1)).findAll();
//        }
//
//        @Test
//        void ShouldReturnEmptyListIfTodoNotExists(){
//
//            when(todoRepository.findAll()).thenReturn(Collections.emptyList());
//
//            List<TodoEntity> result = todoService.findAllTodos();
//
//            assertTrue(result.isEmpty());
//            verify(todoRepository, times(1)).findAll();
//        }
//
//        @Test
//        void ShouldThrowsExceptionWhenRepositoryThrows(){
//            when(todoRepository.findAll()).thenThrow(new RuntimeException("Erro no banco de dados"));
//            assertThrows(RuntimeException.class, () -> todoService.findAllTodos());
//            verify(todoRepository, times(1)).findAll();
//        }
//
//    }
//
//    @Nested
//    class FindTodoById{
//
//        UUID id = UUID.randomUUID();
//
//        @Test
//        void ShouldReturnIdIfTodoExists(){
//            TodoEntity mockTodo = new TodoEntity("Tarefa 1", "Descrição 1");
//            mockTodo.setId(id);
//
//            when(todoRepository.findById(id)).thenReturn(Optional.of(mockTodo));
//
//            TodoEntity result = todoService.findTodoById(id);
//
//            assertEquals(mockTodo, result);
//            verify(todoRepository, times(1)).findById(id);
//        }
//
//        @Test
//        void ShouldThrowNotFoundIdWhenIdDoesNotExists(){
//
//            when(todoRepository.findById(id)).thenReturn(Optional.empty());
//
//            assertThrows(NotFoundId.class, () -> todoService.findTodoById(id));
//
//            verify(todoRepository, times(1)).findById(id);
//        }
//
//        @Test
//        void ShouldThrowsExceptionWhenRepositoryThrows() {
//
//            when(todoRepository.findById(id)).thenThrow(new RuntimeException("Erro no banco de dados"));
//
//            assertThrows(RuntimeException.class, () -> todoService.findTodoById(id));
//            verify(todoRepository, times(1)).findById(id);
//        }
//
//    }
//
//    @Nested
//    class DeleteTodoById {
//
//        UUID id = UUID.randomUUID();
//
//        @Test
//        void ShouldDeleteTodoIfIdExists(){
//
//            when(todoRepository.existsById(id)).thenReturn(true);
//
//            todoService.deleteTodoById(id);
//
//            verify(todoRepository).existsById(id);
//            verify(todoRepository).deleteById(id);
//        }
//
//        @Test
//        void ShouldDoNothingIfIdDoesNotExists(){
//
//            when(todoRepository.existsById(id)).thenReturn(false);
//
//            todoService.deleteTodoById(id);
//
//            verify(todoRepository).existsById(id);
//            verify(todoRepository, never()).deleteById(id);
//        }
//
//        @Test
//        void ShouldThrowsExceptionWhenRepositoryThrows() {
//
//            when(todoRepository.existsById(id)).thenThrow(new RuntimeException("Erro no banco de dados"));
//
//            assertThrows(RuntimeException.class, () -> todoService.deleteTodoById(id));
//            verify(todoRepository, never()).deleteById(id);
//        }
//    }
//
//    @Nested
//    class UpdateTodoById{
//
//        UUID id = UUID.randomUUID();
//
//        @Test
//        void ShouldUpdateWhenFieldsProvided(){
//            RequestUpdateTodoByIdDto req = new RequestUpdateTodoByIdDto("new_name","new_description", TodoStatus.COMPLETED);
//
//            TodoEntity existingEntity = new TodoEntity("old_name", "old_description");
//
//            when(todoRepository.findById(id)).thenReturn(Optional.of(existingEntity));
//            when(todoRepository.save(existingEntity)).thenReturn(existingEntity);
//
//            TodoEntity updatedEntity = todoService.updateTodoById(id, req);
//
//            assertEquals(req.name(), updatedEntity.getName());
//            assertEquals(req.description(), updatedEntity.getDescription());
//            assertEquals(req.status(), updatedEntity.getStatus());
//
//            verify(todoRepository).findById(id);
//            verify(todoRepository).save(existingEntity);
//        }
//
//        @Test
//        void ShouldThrowNotFoundIdWhenNotFoundId(){
//
//            when(todoRepository.findById(id)).thenReturn(Optional.empty());
//
//            assertThrows(NotFoundId.class, () ->
//                todoService.updateTodoById(id, new RequestUpdateTodoByIdDto(null, null, null))
//            );
//
//            verify(todoRepository, never()).save(any());
//        }
//
//        @Test
//        void ShouldThrowsExceptionWhenRepositoryThrows(){
//
//            when(todoRepository.findById(id))
//                    .thenThrow(new RuntimeException("Erro no banco de dados"));
//
//            assertThrows(RuntimeException.class, () ->
//                    todoService.updateTodoById(id, new RequestUpdateTodoByIdDto(null, null, null))
//            );
//
//            verify(todoRepository, never()).save(any());
//        }
//    }
//}
//
