package com.pcfinal.apiapp.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/redirect")
public class RedirectController {
    
    @GetMapping("/")
    public String home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return "redirect:/web/dashboard";
        } else {
            return "redirect:/web/login";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/web/dashboard";
    }
}
