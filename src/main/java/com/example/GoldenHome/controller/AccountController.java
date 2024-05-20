package com.example.GoldenHome.controller;

import com.example.GoldenHome.domain.model.request.RegisterRequest;
import com.example.GoldenHome.domain.model.request.SignInRequest;
import com.example.GoldenHome.domain.model.response.SignInResponse;
import com.example.GoldenHome.service.JwtAuthService;
import com.example.GoldenHome.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {

    JwtAuthService jwtAuthService;

    UserService userService;

    @GetMapping("/")
    public ModelAndView showLoginPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, RedirectAttributes redirectAttributes) {
        return new ModelAndView("AccountPage");
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String response = userService.doRegister(registerRequest);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/auth-token")
    public ResponseEntity<SignInResponse> DoLogin(HttpServletResponse httpResponse, @RequestBody SignInRequest signInRequest) {
        SignInResponse response = jwtAuthService.authenticate(signInRequest);

        Cookie cookie = new Cookie("authorization", response.getToken());
        cookie.setMaxAge(60 * 60);
        httpResponse.addCookie(cookie);
        httpResponse.addHeader("authorization", response.getToken());

        return new ResponseEntity<>(response, response.getStatus());
    }
}
