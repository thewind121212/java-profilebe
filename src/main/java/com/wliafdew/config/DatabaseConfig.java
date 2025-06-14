package com.wliafdew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.wliafdew.repo.Database;
import com.wliafdew.repo.TodoRepository;

@Configuration
public class DatabaseConfig {

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
} 