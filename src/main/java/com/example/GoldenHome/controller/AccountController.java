package com.example.GoldenHome.controller;

import com.example.GoldenHome.components.common.Const;
import com.example.GoldenHome.components.model.request.RegisterRequest;
import com.example.GoldenHome.components.model.request.SignInRequest;
import com.example.GoldenHome.components.model.response.SignInResponse;
import com.example.GoldenHome.components.util.DateTimeUtils;
import com.example.GoldenHome.service.JwtAuthService;
import com.example.GoldenHome.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.GoldenHome.components.util.ControllerUtils.doCheckValidFields;
import static com.example.GoldenHome.components.util.ControllerUtils.getToken;

@Controller
@RequestMapping(path = "")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {

    JwtAuthService jwtAuthService;

    UserService userService;

    @GetMapping("/")
    public ModelAndView showLoginPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse, RedirectAttributes redirectAttributes) {
        Cookie[] cookies = httpRequest.getCookies();
        String refreshToken = getToken(cookies, "refreshToken");
        String accessToken = getToken(cookies, "accessToken");
        if (jwtAuthService.verifyRefreshToken(refreshToken, accessToken))
            return new ModelAndView("redirect:/newsfeed");
        return new ModelAndView("AccountPage");
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = doCheckValidFields(bindingResult);
        if (errors != null) return errors;

        return userService.register(registerRequest);
    }

    @PostMapping("/auth-token")
    public ResponseEntity<Object> DoLogin(HttpServletResponse httpResponse, HttpServletRequest httpRequest,
                                          @Valid @RequestBody SignInRequest signInRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = doCheckValidFields(bindingResult);
        if (errors != null) return errors;

        SignInResponse response = jwtAuthService.authenticate(signInRequest);
        if (response.hasError()) {
            return new ResponseEntity<>(response.getErrors(), response.getStatus());
        }
        Cookie cookie = new Cookie("accessToken", response.getAccessToken());
        cookie.setMaxAge(DateTimeUtils.calculateSeconds(5, 0));
        httpResponse.addCookie(cookie);

        if (signInRequest.isRemember()) {
            Cookie refreshToken = new Cookie("refreshToken", response.getRefreshToken());
            cookie.setMaxAge(DateTimeUtils.calculateSeconds(2, 0, 0, 0));

            httpResponse.addCookie(refreshToken);
        }
        return new ResponseEntity<>(null, response.getStatus());
    }
}
