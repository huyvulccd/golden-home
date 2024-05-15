package com.example.timphongtrohanoi.domain.configuration;

import com.nimbusds.jose.JWSAlgorithm;
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
public class SecurityConfig {
    private final String[] endpointAuth =
            {"/auth-introspect", "/auth-token", "/registration"};
    private final String[] endpointSource =
            {"/css/**", "/js/**", "/fonts/**", "img/**"};

    @Value("${jwt.signerKey")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.GET, endpointSource).permitAll()
                                .requestMatchers(HttpMethod.POST, endpointAuth).permitAll()
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/").permitAll()
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
