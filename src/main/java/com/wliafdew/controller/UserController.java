package com.wliafdew.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wliafdew.exception.ResourceNotFoundException;
import com.wliafdew.model.users;
import com.wliafdew.service.impl.usersServiceImpl;

@RestController
@RequestMapping("/api/users")
public class UserController {

    usersServiceImpl usersServiceImpl;

    @Autowired
    public void setUsersServiceImpl(usersServiceImpl usersServiceImpl) {
        this.usersServiceImpl = usersServiceImpl;
    }

    @GetMapping
    public List<users> getAllUsers() {
        return usersServiceImpl.getUsers();
    }

    @PostMapping
    public ResponseEntity<users> createUsers(@RequestBody users user) {
        return ResponseEntity.ok(usersServiceImpl.insertUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<users> getUserById(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(usersServiceImpl.getUserByID(id));
    }
}
