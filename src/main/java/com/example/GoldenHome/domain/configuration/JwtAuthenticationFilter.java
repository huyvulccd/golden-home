package com.example.GoldenHome.domain.configuration;

import com.example.GoldenHome.domain.model.CustomUserDetails;
import com.example.GoldenHome.service.JwtAuthService;
import com.example.GoldenHome.service.UserService;
import com.mysql.cj.util.StringUtils;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtAuthService jwtAuthService;

    UserService userService;

    String[] AUTH_ENDPOINTS =
            {"/auth-introspect", "/auth-token", "/registration"};
    String[] SOURCE_ENDPOINTS =
            {"/css/", "/js/", "/fonts/", "img/", "/favicon.ico"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String requestURI = request.getRequestURI();
        if (!isIgnoredUrl(requestURI)) {
            String jwtToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null)
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("authorization")) {
                        jwtToken = cookie.getValue();
                    }
                }

            String username = null;
            if (!StringUtils.isNullOrEmpty(jwtToken)) {
                try {
                    username = jwtAuthService.extractUsername(jwtToken);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if (username != null & SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = userService.loadUserByUsername(username);
                try {
                    if (jwtAuthService.introspect(jwtToken, userDetails)) {
    //                    if (requestURI.equals("/")) {
    //                        response.sendRedirect("/newsfeed");
    //                    }
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    } else {
                        System.out.println("Token is not validate");
                    }
                } catch (JOSEException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isIgnoredUrl(String requestURI) {
        for (String endpoint : AUTH_ENDPOINTS) {
            if (requestURI.equals(endpoint)) {
                return true;
            }
        }
        for (String endpoint : SOURCE_ENDPOINTS) {
            if (requestURI.startsWith(endpoint)) {
                return true;
            }
        }
        return false;
    }
}
