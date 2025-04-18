package com.springboot.backend.luchin.userapp.user_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.luchin.userapp.user_backend.entities.User;
import com.springboot.backend.luchin.userapp.user_backend.repositories.UserRepository;

@Service
public class UserJPADetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> optionalUser = repository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }
        User user = optionalUser.orElseThrow();
        List<GrantedAuthority> autorities = user.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), true, true, true, true, autorities);
        // return new userdetails.User(username, user.getPassword(), true, true, true, true, authorities);
    }
}
