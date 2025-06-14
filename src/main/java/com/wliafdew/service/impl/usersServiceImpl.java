package com.wliafdew.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wliafdew.exception.ResourceNotFoundException;
import com.wliafdew.model.users;
import com.wliafdew.repo.usersRepository;
import com.wliafdew.service.usersService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class usersServiceImpl implements usersService {

    usersRepository usersRepository;

    private EntityManager entityManager;

    @Autowired
    public void setUsersRepository(usersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<users> getUsers() {
        return usersRepository.findAll();
    }

    @Override
    public users getUserByID(UUID id) throws ResourceNotFoundException {
        users user = usersRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User not found for this id: " + id)
        );
        return user;
    }

    @Override
    public users insertUser(users user) {
        user.setId(null);
        return usersRepository.save(user);
    }

    @Override
    public users updateUsers(users user) throws ResourceNotFoundException {
        usersRepository.findById(user.getId()).orElseThrow(
            () -> new ResourceNotFoundException("User not found for this id: " + user.getId())
        );

        //encrypt password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordString = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordString);
        //updare user
        usersRepository.save(user);
        users userUpdated = this.getUserByID(user.getId());
        return userUpdated;
    }

    @Override
    public void deleteUser(UUID id) throws ResourceNotFoundException {
        usersRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User not found for this id: " + id)
        );
        usersRepository.deleteById(id);
    }
}
