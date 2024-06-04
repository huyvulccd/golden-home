package com.example.GoldenHome.components.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {
    String[] AUTH_ENDPOINTS =
            {"/", "/auth-introspect", "/auth-token", "/registration"};
    String[] SOURCE_ENDPOINTS =
            {"/css/**", "/js/**", "/fonts/**", "img/**"};
    String[] PROTECTED_ENDPOINTS =
            {"/newsfeed/**"};

    JwtAuthenticationFilter jwtFilter;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.GET, SOURCE_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.POST, AUTH_ENDPOINTS).permitAll()
                                .requestMatchers(PROTECTED_ENDPOINTS).authenticated()
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/").permitAll()
                        .permitAll())
                .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout  -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("accessToken")
                        .deleteCookies("refreshToken")
                        .logoutSuccessUrl("/")
                )
                .passwordManagement((management) -> management.changePasswordPage("/account/change-password"))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
