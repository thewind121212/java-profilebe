package com.wliafdew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.wliafdew.repo.ApiLogRepository;
import com.wliafdew.repo.Database;
import com.wliafdew.repo.TodoRepository;

@Configuration
public class RepoConfig {

    @Bean
    public Database database(Environment env) {
        Database db = new Database(env);
        db.connectToDB();
        return db;
    }

    @Bean
    public TodoRepository todoRepository(Database database) {
        return new TodoRepository(database);
    }

    @Bean
    public ApiLogRepository apiLogRepository(Database database) {
        return new ApiLogRepository(database);
    }
} 