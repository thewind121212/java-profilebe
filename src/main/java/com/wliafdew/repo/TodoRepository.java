package com.wliafdew.repo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.wliafdew.model.Todo;

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
                        rs.getObject("id", UUID.class),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("isDone")));
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
                            rs.getObject("id", UUID.class),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBoolean("isDone")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(Todo todo) {
        try (var conn = database.getConnection()) {
            try (var stmt = conn.prepareStatement(
                    "INSERT INTO todos (title, description, isDone) VALUES (?, ?, ?)")) {
                stmt.setString(1, todo.getTitle());
                stmt.setString(2, todo.getDescription());
                stmt.setBoolean(3, todo.isDone());
                stmt.executeUpdate();
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