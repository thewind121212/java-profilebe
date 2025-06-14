package com.wliafdew.repo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.wliafdew.model.Todo;
import com.wliafdew.repo.Database;

@Repository
public class TodoRepository {
    private final Database database;

    public TodoRepository(Database database) {
        this.database = database;
    }

    public List<Todo> findAll() {
        List<Todo> todos = new ArrayList<>();
        try (var conn = database.getConnection();
             var stmt = conn.prepareStatement("SELECT * FROM todos");
             var rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                todos.add(new Todo(
                    (UUID) rs.getObject("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getBoolean("isDone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    public Optional<Todo> findById(UUID id) {
        try (var conn = database.getConnection();
             var stmt = conn.prepareStatement("SELECT * FROM todos WHERE id = ?::uuid")) {
            
            stmt.setObject(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Todo(
                        (UUID) rs.getObject("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("isDone")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(Todo todo) {
        try (var conn = database.getConnection()) {
            if (todo.getId() == null) {
                // Insert
                try (var stmt = conn.prepareStatement(
                    "INSERT INTO todos (id, title, description, isDone) VALUES (?::uuid, ?, ?, ?)")) {
                    stmt.setObject(1, todo.getId());
                    stmt.setString(2, todo.getTitle());
                    stmt.setString(3, todo.getDescription());
                    stmt.setBoolean(4, todo.isDone());
                    stmt.executeUpdate();
                }
            } else {
                // Update
                try (var stmt = conn.prepareStatement(
                    "UPDATE todos SET title = ?, description = ?, isDone = ? WHERE id = ?::uuid")) {
                    stmt.setString(1, todo.getTitle());
                    stmt.setString(2, todo.getDescription());
                    stmt.setBoolean(3, todo.isDone());
                    stmt.setObject(4, todo.getId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID id) {
        try (var conn = database.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM todos WHERE id = ?::uuid")) {
            stmt.setObject(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 