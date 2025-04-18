package com.springboot.backend.luchin.userapp.user_backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.luchin.userapp.user_backend.entities.Role;
import com.springboot.backend.luchin.userapp.user_backend.entities.User;
import com.springboot.backend.luchin.userapp.user_backend.models.IUser;
import com.springboot.backend.luchin.userapp.user_backend.models.UserRequest;
import com.springboot.backend.luchin.userapp.user_backend.repositories.RoleRepository;
import com.springboot.backend.luchin.userapp.user_backend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository repository;
    private RoleRepository roleRepository;

    // @Autowired //se lo usa si no estuviera passwordEncoder en el constructor
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List) this.repository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User u) {
        u.setRoles(getRoles(u));
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return this.repository.save(u);
    }
    
    @Override
    @Transactional
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = repository.findById(id);
        if(userOptional.isPresent()){
            User userBD = userOptional.get();
            userBD.setEmail(user.getEmail());
            userBD.setLastname(user.getLastname());
            userBD.setName(user.getName());
            userBD.setUsername(user.getUsername());
            List<Role> roles = getRoles(user);
            userBD.setRoles(roles);
            return Optional.of(repository.save(userBD));
            // return ResponseEntity.ok(service.save(userBD));
        }
        return Optional.empty();
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        // optionalRole.ifPresent(role -> roles.add(role)); //hace lo mismo que la linea de abajo
        optionalRole.ifPresent(roles::add);
        if(user.isAdmin()){
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            // optionalRole.ifPresent(role -> roles.add(role)); //hace lo mismo que la linea de abajo
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }


}
