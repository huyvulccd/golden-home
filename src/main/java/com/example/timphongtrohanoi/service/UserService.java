package com.example.timphongtrohanoi.service;

import com.example.timphongtrohanoi.domain.entities.User;
import com.example.timphongtrohanoi.domain.model.CustomUserDetails;
import com.example.timphongtrohanoi.domain.queries.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
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


}
