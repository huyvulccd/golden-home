package com.example.timphongtrohanoi.service;

import com.example.timphongtrohanoi.domain.entities.User;
import com.example.timphongtrohanoi.domain.model.CustomUserDetails;
import com.example.timphongtrohanoi.domain.model.request.RegisterRequest;
import com.example.timphongtrohanoi.domain.queries.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByUsername(username);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(optUser.get());
    }


    public User saveUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole((byte) 0);
        user.setUsername(request.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);

    }

    public String doRegister(RegisterRequest request) throws DataIntegrityViolationException {

        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new DataIntegrityViolationException("User with username " + request.getUsername() + " already exists");
        }
        return saveUser(request).getUsername();
    }
}
