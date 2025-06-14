package com.wliafdew.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.wliafdew.model.Todo;

public interface TodoService {
    List<Todo> getAllTodos();
    Optional<Todo> getTodoById(UUID id);
    Todo createTodo(Todo todo); 
    void updateTodo(UUID id, Todo updatedTodo);
    void deleteTodo(UUID id);
    List<Todo> getCompletedTodos();
    List<Todo> getIncompleteTodos();
    void toggleTodoStatus(UUID id);
} 