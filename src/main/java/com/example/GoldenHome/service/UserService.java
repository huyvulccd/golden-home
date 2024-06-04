package com.example.GoldenHome.service;

import com.example.GoldenHome.components.model.CustomUserDetails;
import com.example.GoldenHome.components.model.entities.User;
import com.example.GoldenHome.components.model.request.RegisterRequest;
import com.example.GoldenHome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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

    public ResponseEntity<Object> register(RegisterRequest request) {

        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            String msg = "User with username " + request.getUsername() + " already exists";
            return new ResponseEntity<>(Map.of("result", msg), HttpStatus.BAD_REQUEST);
        }
        String username = saveUser(request).getUsername();
        return new ResponseEntity<>(username, HttpStatus.ACCEPTED);
    }
}
