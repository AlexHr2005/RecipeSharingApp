package com.recipeshare.project.repositories;

import com.recipeshare.project.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
}
