package com.springboot.backend.luchin.userapp.user_backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.luchin.userapp.user_backend.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
    Page<User> findAll(Pageable Pageable);
    Optional<User> findByUsername(String name);
}
