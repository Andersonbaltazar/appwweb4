package com.pcfinal.apiapp.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebHomeController {
    
    @GetMapping("/home")
    public String home() {
        return "redirect:/web/dashboard";
    }
}
