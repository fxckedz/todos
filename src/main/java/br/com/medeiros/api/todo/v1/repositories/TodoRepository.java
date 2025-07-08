package br.com.medeiros.api.todo.v1.repositories;

import br.com.medeiros.api.todo.v1.entities.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {}
