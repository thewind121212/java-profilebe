package com.wliafdew.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wliafdew.model.users;

@Repository
public interface UsersRepository extends JpaRepository<users, UUID> {
}
