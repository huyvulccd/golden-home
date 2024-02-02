package com.example.timphongtrohanoi.controller;

import com.example.timphongtrohanoi.controller.model.UserSignup;
import com.example.timphongtrohanoi.service.UserService;
import com.example.timphongtrohanoi.service.dto.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserService service;

    @GetMapping()
    public ModelAndView showLoginPage(Model model, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("signup");
        mav.addObject("userSignup", new UserSignup());
        return mav;
    }

    @PostMapping()
    public ModelAndView actionRegister(@ModelAttribute("userSignup") UserSignup userSignup,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView("signup");
        Validate validate = service.doValidateFormSubmit(userSignup, bindingResult);

        if (validate.hasErrors()) {
            mav.setViewName("redirect:/signup");
            redirectAttributes.addFlashAttribute("listError", validate.getMessages());
            redirectAttributes.addFlashAttribute("userSignup", userSignup);
        }
        return mav;
    }

}
