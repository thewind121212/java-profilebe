package com.wliafdew.service;

import java.util.List;
import java.util.UUID;

import com.wliafdew.exception.ResourceNotFoundException;
import com.wliafdew.model.users;

public interface usersService {
    List<users> getUsers();

    users getUserByID(UUID id) throws ResourceNotFoundException;

    users insertUser(users user);

    users updateUsers(users user) throws ResourceNotFoundException;

    void deleteUser(UUID id) throws ResourceNotFoundException;
}
