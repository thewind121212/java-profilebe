/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.wliafdew.repo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

class Todo {
    int id;
    String title;
    String description;
    boolean isDone;
}


class Result {
    Todo todo;
    boolean isError;
    Result(Todo todo, boolean isError) {
        this.todo = todo;
        this.isError = isError;
    }
}

/**
 *
 * @author kotomiichinose
 */


public class Repo {
    private static final String URL = "jdbc:postgresql://localhost:5992/todo_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "yourpassword";
    public static Boolean isConnected = false;

    private static final Map<String, String> TABLE_DEFINITIONS = Map.of(
        "todos", """
            CREATE TABLE todos (
                id SERIAL PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description TEXT,
                isDone BOOLEAN DEFAULT FALSE
            )
            """
    );

    private static final List<String> TABLES = List.of("todos");

    public static void createTable(String tableName) {
        String createSql = TABLE_DEFINITIONS.get(tableName);
        if (createSql == null) {
            System.out.println("Table definition not found for: " + tableName);
            return;
        }

        String checkSql = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = '" + tableName + "')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             ResultSet rs = checkStmt.executeQuery()) {
            
            if (rs.next() && rs.getBoolean(1)) {
                System.out.println("Table '" + tableName + "' already exists");
                return;
            }

            try (PreparedStatement createStmt = conn.prepareStatement(createSql)) {
                createStmt.executeUpdate();
                System.out.println("Table '" + tableName + "' created successfully");
            }
        } catch (SQLException e) {
            System.out.println("Error checking/creating table: " + e.getMessage());
        }
    }

    public static void connectToDB() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {
                System.out.println("DB connected");
                isConnected = true;
                for (String tableName : TABLES) {
                    createTable(tableName);
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            isConnected = false;
        }
    }

    public static Result createTodo(Todo todo) {
        if (!isConnected) {
            return new Result(null, true);
        }
        String sql = "INSERT INTO todos (title, description, isDone) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, todo.title);
            pstmt.setString(2, todo.description);
            pstmt.setBoolean(3, todo.isDone);
            pstmt.executeUpdate();
            return new Result(todo, false);
        } catch (SQLException e) {
            System.out.println("Failed to create todo: " + e.getMessage());
            return new Result(null, true);
        }
    }

    public static Result updateTodo(Todo todo) {
        if (!isConnected) {
            return new Result(null, true);
        }
        String sql = "UPDATE todos SET title = ?, description = ?, isDone = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, todo.title);
            pstmt.setString(2, todo.description);
            pstmt.setBoolean(3, todo.isDone);
            pstmt.setInt(4, todo.id);
            pstmt.executeUpdate();
            return new Result(todo, false);
        } catch (SQLException e) {
            System.out.println("Failed to update todo: " + e.getMessage());
            return new Result(null, true);
        }
    }

    public static Result deleteTodo(int id) {
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return new Result(null, false);
        } catch (SQLException e) {
            System.out.println("Failed to delete todo: " + e.getMessage());
            return new Result(null, true);
        }
    }
}
