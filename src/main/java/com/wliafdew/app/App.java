package com.wliafdew.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@ComponentScan(basePackages = "com.wliafdew")
@EntityScan("com.wliafdew")
@CrossOrigin(origins = "*")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
