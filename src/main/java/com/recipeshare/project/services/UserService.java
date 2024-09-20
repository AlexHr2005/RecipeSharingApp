package com.recipeshare.project.services;

import com.recipeshare.project.dto.RegistrationDto;
import com.recipeshare.project.models.Role;
import com.recipeshare.project.models.UserEntity;
import com.recipeshare.project.repositories.RoleRepository;
import com.recipeshare.project.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = mapRegistrationDtoToUser(registrationDto);
        Role role = roleRepository.findByName("USER");
        user.addRole(role);
        userRepository.save(user);
    }

    private UserEntity mapRegistrationDtoToUser(RegistrationDto registrationDto) {
        return UserEntity.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .roles(new ArrayList<>())
                .build();
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
