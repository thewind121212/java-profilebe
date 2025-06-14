package com.wliafdew.domain.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wliafdew.model.Todo;
import com.wliafdew.repo.TodoRepository;
import com.wliafdew.service.TodoService;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Optional<Todo> getTodoById(UUID id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo createTodo(Todo todo) {
        todoRepository.save(todo);
        return todo;
    }

    @Override
    public void updateTodo(UUID id, Todo updatedTodo) {
        Optional<Todo> existingTodo = todoRepository.findById(id);
        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setTitle(updatedTodo.getTitle());
            todo.setDescription(updatedTodo.getDescription());
            todo.setDone(updatedTodo.isDone());
            todoRepository.save(todo);
        }
    }

    @Override
    public void deleteTodo(UUID id) {
        todoRepository.delete(id);
    }

    @Override
    public List<Todo> getCompletedTodos() {
        return todoRepository.findAll().stream()
            .filter(Todo::isDone)
            .toList();
    }

    @Override
    public List<Todo> getIncompleteTodos() {
        return todoRepository.findAll().stream()
            .filter(todo -> !todo.isDone())
            .toList();
    }

    @Override
    public void toggleTodoStatus(UUID id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            Todo existingTodo = todo.get();
            existingTodo.setDone(!existingTodo.isDone());
            todoRepository.save(existingTodo);
        }
    }
} 