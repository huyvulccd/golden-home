package com.example.timphongtrohanoi.domain.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/css/**", "/js/**", "/fonts/**", "img/**")
                        .permitAll().anyRequest().authenticated())
//                .cors(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin.loginPage("/").permitAll())
                .rememberMe(withDefaults()).logout(withDefaults())
                .passwordManagement((management) -> management.changePasswordPage("/account/change-password"));
        return http.build();
    }
}