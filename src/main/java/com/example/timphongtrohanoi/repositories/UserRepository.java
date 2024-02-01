package com.example.timphongtrohanoi.repositories;

import com.example.timphongtrohanoi.repositories.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
