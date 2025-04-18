package com.springboot.backend.luchin.userapp.user_backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.luchin.userapp.user_backend.entities.Role;


public interface RoleRepository extends CrudRepository<Role, Long>{
    Optional<Role> findByName(String name);

}
