package com.example.timphongtrohanoi.controller;

import com.example.timphongtrohanoi.domain.model.request.IntrospectRequest;
import com.example.timphongtrohanoi.domain.model.request.RegisterRequest;
import com.example.timphongtrohanoi.domain.model.request.SignInRequest;
import com.example.timphongtrohanoi.domain.model.response.IntrospectResponse;
import com.example.timphongtrohanoi.domain.model.response.SignInResponse;
import com.example.timphongtrohanoi.service.JwtAuthService;
import com.example.timphongtrohanoi.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ModelAndView showLoginPage(Model model, RedirectAttributes redirectAttributes) {
        return new ModelAndView("AccountPage");
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String response = userService.doRegister(registerRequest);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/auth-token")
    public ResponseEntity<SignInResponse> DoLogin(@RequestBody SignInRequest request) {
        SignInResponse response;
        try {
            response = jwtAuthService.authenticate(request);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(301));
        }

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/auth-introspect")
    public ResponseEntity<IntrospectResponse> authenticate(@RequestBody IntrospectRequest introspectRequest){
        IntrospectResponse introspectResponse;

        try {
            introspectResponse = jwtAuthService.introspect(introspectRequest);
            return new ResponseEntity<>(introspectResponse, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(IntrospectResponse.builder().build(), HttpStatusCode.valueOf(200));
        }
    }
}
