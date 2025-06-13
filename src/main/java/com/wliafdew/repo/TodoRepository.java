package com.wliafdew.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM todos")) {
            
            while (rs.next()) {
                todos.add(new Todo(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getBoolean("isDone")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching todos: " + e.getMessage());
        }
        return todos;
    }

    public Todo findById(Long id) {
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos WHERE id = ?")) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Todo(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getBoolean("isDone")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching todo: " + e.getMessage());
        }
        return null;
    }

    public Todo save(Todo todo) {
        String sql = todo.getId() == null ?
            "INSERT INTO todos (title, description, isDone) VALUES (?, ?, ?)" :
            "UPDATE todos SET title = ?, description = ?, isDone = ? WHERE id = ?";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, todo.getTitle());
            stmt.setString(2, todo.getDescription());
            stmt.setBoolean(3, todo.getIsDone());
            
            if (todo.getId() != null) {
                stmt.setLong(4, todo.getId());
            }

            stmt.executeUpdate();

            if (todo.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        todo.setId(rs.getLong(1));
                    }
                }
            }
            return todo;
        } catch (SQLException e) {
            System.out.println("Error saving todo: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteById(Long id) {
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE id = ?")) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting todo: " + e.getMessage());
            return false;
        }
    }
} 