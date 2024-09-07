package com.recipeshare.project.services;

import com.recipeshare.project.dto.RegistrationDto;
import com.recipeshare.project.models.Role;
import com.recipeshare.project.models.User;
import com.recipeshare.project.repositories.RoleRepository;
import com.recipeshare.project.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void saveUser(RegistrationDto registrationDto) {
        User user = mapRegistrationDtoToUser(registrationDto);
        Role role = roleRepository.findByName("USER");
        user.addRole(role);
        userRepository.save(user);
    }

    private User mapRegistrationDtoToUser(RegistrationDto registrationDto) {
        return User.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(registrationDto.getPassword())
                .roles(new ArrayList<>())
                .build();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
