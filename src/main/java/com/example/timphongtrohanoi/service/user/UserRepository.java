package com.example.timphongtrohanoi.service.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndEmail(String username, String email);
}
