package com.example.timphongtrohanoi.config;

import com.example.timphongtrohanoi.service.JwtService;
import com.example.timphongtrohanoi.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorHeader = request.getHeader("Authorization");
        final String AuthorToken;
        final String username;

        String requestURI = request.getRequestURI();

        String[] URIIgnoreAuthenticate = new String[]{"/login", "/signup", "/css", "/js", "/media", "/favicon.ico"};

        for (String prefixUri : URIIgnoreAuthenticate) {
            if (requestURI.startsWith(prefixUri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (StringUtils.hasText(authorHeader) || authorHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        AuthorToken = authorHeader.substring(7);
        username = jwtService.extractUsername(AuthorToken);

        if (!StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

            if (jwtService.isTokenValid(AuthorToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
