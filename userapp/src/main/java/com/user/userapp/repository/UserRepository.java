package com.user.userapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.userapp.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
