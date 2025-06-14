package com.wliafdew.model;

import java.util.UUID;

public class Todo {
    private UUID id;
    private String title;
    private String description;
    private boolean done;

    public Todo() {
        this.id = UUID.randomUUID();
    }

    public Todo(String title, String description, boolean done) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public Todo(UUID id, String title, String description, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
} 