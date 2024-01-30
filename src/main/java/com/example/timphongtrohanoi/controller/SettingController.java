package com.example.timphongtrohanoi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/setting")
public class SettingController {
    @GetMapping("")
    public ModelAndView loadSettingPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("authorize-demo/Setting");
        return mav;
    }

}
