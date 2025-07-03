package com.pcfinal.apiapp.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/redirect")
public class RedirectController {
    
    @GetMapping("/dashboard")
    public String dashboardRedirect() {
        return "redirect:/web/dashboard";
    }
}
