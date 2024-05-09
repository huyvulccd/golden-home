package com.example.timphongtrohanoi.controller;

import com.example.timphongtrohanoi.domain.model.SignInRequest;
import com.example.timphongtrohanoi.domain.model.SignInResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class AccountController {

    @GetMapping()
    public ModelAndView showLoginPage(Model model, RedirectAttributes redirectAttributes) {
        return new ModelAndView("AccountPage");
    }

    @PostMapping("login")
    public ResponseEntity<SignInResponse> DoLogin(@RequestBody SignInRequest request) {

        return new ResponseEntity<>(HttpStatusCode.valueOf(2));
    }
}
