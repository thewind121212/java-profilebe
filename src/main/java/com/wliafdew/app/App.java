package com.wliafdew.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.wliafdew.controller.TodoController;
import com.wliafdew.repo.Database;
import com.wliafdew.repo.TodoRepository;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

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
    public TodoController todoController(TodoRepository todoRepository) {
        return new TodoController(todoRepository);
    }
}
