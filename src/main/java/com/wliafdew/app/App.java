package com.wliafdew.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wliafdew.repo.Database;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class App {
    @Autowired
    private Database database;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
