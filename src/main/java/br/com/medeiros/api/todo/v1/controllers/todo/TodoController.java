package br.com.medeiros.api.todo.v1.controllers.todo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos/v1")
@Tag(name = "Todos", description = "Endpoints to Managing Todos")
public class TodoController {}
