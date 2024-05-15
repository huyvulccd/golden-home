package com.example.timphongtrohanoi.domain.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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

import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    String[] AUTH_ENDPOINTS =
            {"/auth-introspect", "/auth-token", "/registration"};
    String[] SOURCE_ENDPOINTS =
            {"/css/**", "/js/**", "/fonts/**", "img/**"};
    String[] PROTECTED_ENDPOINTS =
            {"/newsfeed/**"};

    @NonFinal
    @Value("${jwt.signerKey")
    String signerKey;

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
//                        .loginProcessingUrl("/auth-token")
                        .permitAll())
                .oauth2ResourceServer(oath2 -> oath2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())))
                .rememberMe(withDefaults()).logout(withDefaults())
                .passwordManagement((management) -> management.changePasswordPage("/account/change-password"))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

    }
}
