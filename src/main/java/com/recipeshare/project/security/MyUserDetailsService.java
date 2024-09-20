package com.recipeshare.project.security;

import com.recipeshare.project.models.Role;
import com.recipeshare.project.models.UserEntity;
import com.recipeshare.project.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        if(user != null) {
            return new User(
                    user.getUsername(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles())
            );
        }
        else throw new UsernameNotFoundException("The user was not found: " + username);
    }

    public List<SimpleGrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList());
    }
}
