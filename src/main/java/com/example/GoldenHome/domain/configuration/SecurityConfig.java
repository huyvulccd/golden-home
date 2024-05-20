package com.example.GoldenHome.domain.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import static org.springframework.security.config.Customizer.withDefaults;

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

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    JwtAuthenticationFilter jwtFilter;

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
                .rememberMe(withDefaults()).logout(withDefaults())
                .logout(logout  -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("authorization", "JSESSIONID")
                        .invalidateHttpSession(true)
                )
                .passwordManagement((management) -> management.changePasswordPage("/account/change-password"))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);
        return http.build();

    }
}
