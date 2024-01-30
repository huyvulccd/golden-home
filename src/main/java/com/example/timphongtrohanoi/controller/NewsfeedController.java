package com.example.timphongtrohanoi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/newsfeed")
public class NewsfeedController {
    @GetMapping("")
    public ModelAndView loadNewsfeedPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("authorize-demo/Tenant");
        return mav;
    }

}
