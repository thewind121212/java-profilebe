/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.wliafdew.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author kotomiichinose
 */

@Component
public class Database {
    private final String url;
    private final String username;
    private final String password;
    private boolean isConnected = false;

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

    public Database(
        @Value("${spring.datasource.url}") String url,
        @Value("${spring.datasource.username}") String username,
        @Value("${spring.datasource.password}") String password
    ) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void createTable(String tableName) {
        String createSql = TABLE_DEFINITIONS.get(tableName);
        if (createSql == null) {
            System.out.println("Table definition not found for: " + tableName);
            return;
        }

        try (Connection conn = getConnection()) {
            String checkSql = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)";
            try (var checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, tableName);
                try (var rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getBoolean(1)) {
                        System.out.println("Table '" + tableName + "' already exists");
                        return;
                    }
                }
            }

            try (var createStmt = conn.prepareStatement(createSql)) {
                createStmt.executeUpdate();
                System.out.println("Table '" + tableName + "' created successfully");
            }
        } catch (SQLException e) {
            System.out.println("Error checking/creating table: " + e.getMessage());
        }
    }

    public void connectToDB() {
        try (Connection conn = getConnection()) {
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
}
