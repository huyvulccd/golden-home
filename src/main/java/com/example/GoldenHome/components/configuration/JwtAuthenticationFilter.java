package com.example.GoldenHome.components.configuration;

import com.example.GoldenHome.components.common.Const;
import com.example.GoldenHome.components.model.CustomUserDetails;
import com.example.GoldenHome.components.util.DateTimeUtils;
import com.example.GoldenHome.service.JwtAuthService;
import com.example.GoldenHome.service.UserService;
import com.mysql.cj.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

import static com.example.GoldenHome.components.util.ControllerUtils.getToken;

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
            Cookie[] cookies = request.getCookies();
            String accessToken = getToken(cookies, "accessToken");
            String refreshToken = getToken(cookies, "refreshToken");

            String username = null;
            boolean accessTokenIsAvailable = !StringUtils.isNullOrEmpty(accessToken);
            if (accessTokenIsAvailable
                    || !StringUtils.isNullOrEmpty(refreshToken)) {
                try {
                    username = jwtAuthService.extractUsername(accessToken, refreshToken);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if (username != null & SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = userService.loadUserByUsername(username);
                if (accessTokenIsAvailable) {
                    int statusIntrospected = jwtAuthService.introspectAccessToken(accessToken);
                    if (statusIntrospected == Const.JWTStatus.ACCEPT_AT.getCode()) {
                        authenticateSuccess(request, userDetails);
                    }
                } else {
                    int statusRF = jwtAuthService.verifyRefreshToken(refreshToken);
                    if (statusRF != Const.JWTStatus.INVALID_RT.getCode()) {
                        setCookie(response, statusRF, userDetails);
                        authenticateSuccess(request, userDetails);
                    }
                }
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCookie(HttpServletResponse response, int statusRF, CustomUserDetails userDetails) {
        String accessToken = jwtAuthService.generateAccessToken(userDetails.getUsername());
        Cookie cookieAT = new Cookie("accessToken", accessToken);
        cookieAT.setMaxAge(DateTimeUtils.calculateSeconds(5, 0));
        response.addCookie(cookieAT);

        if (statusRF == Const.JWTStatus.NEED_RENEW_RT.getCode()) {
            String refreshToken = jwtAuthService
                    .generateRefreshToken(userDetails.getUsername(), userDetails.getPassword());

            Cookie cookieRT = new Cookie("refreshToken", refreshToken);
            cookieRT.setMaxAge(DateTimeUtils.calculateSeconds(2, 0, 0, 0));
            response.addCookie(cookieRT);
        }
    }

    private static void authenticateSuccess(HttpServletRequest request, CustomUserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
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
