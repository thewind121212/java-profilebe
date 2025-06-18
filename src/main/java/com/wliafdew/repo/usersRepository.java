package com.wliafdew.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wliafdew.model.users;

@Repository
public interface UsersRepository extends JpaRepository<users, UUID> {

    @Query(value="select * from users u where u.email = :email ", nativeQuery = true)
    users getUserByEmail(@Param(value="email") String email);
}
