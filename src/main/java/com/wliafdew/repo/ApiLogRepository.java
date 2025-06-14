package com.wliafdew.repo;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.wliafdew.model.ApiLog;

@Repository
public class ApiLogRepository {
    private final Database database;

    public ApiLogRepository(Database database) {
        this.database = database;
    }

    public void save(ApiLog apiLog) {
        try (var conn = database.getConnection();
                var stmt = conn.prepareStatement("INSERT INTO api_logs (class_name, method_name, start_time, end_time, execution_time, status, error_message, stack_trace) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, apiLog.getClassName());
            stmt.setString(2, apiLog.getMethodName());
            stmt.setObject(3, apiLog.getStartTime());
            stmt.setObject(4, apiLog.getEndTime());
            stmt.setLong(5, apiLog.getExecutionTime());
            stmt.setString(6, apiLog.getStatus());
            stmt.setString(7, apiLog.getErrorMessage());
            stmt.setString(8, apiLog.getStackTrace());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

