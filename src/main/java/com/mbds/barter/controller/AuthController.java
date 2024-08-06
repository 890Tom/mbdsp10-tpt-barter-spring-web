package com.mbds.barter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mbds.barter.exception.BadRequestException;
import com.mbds.barter.exception.InternalServerException;
import com.mbds.barter.request.AuthRequest;
import com.mbds.barter.response.AuthResponse;
import com.mbds.barter.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
	
	@Autowired
    private AuthService authService;
	
	@GetMapping("/login")
    public String showloginForm(Model model) {
		model.addAttribute("authRequest", new AuthRequest());
        return "login";
    }
	
	@PostMapping("/login")
	public String submitLoginForm(@ModelAttribute AuthRequest authRequest, Model model, HttpSession session) {
		
		try {
			AuthResponse authResponse = authService.login(authRequest);
			session.setAttribute("authResponse", authResponse);
			return "redirect:/categories";
		}catch(BadRequestException | InternalServerException e) {
			model.addAttribute("error", e.getMessage());
			return "login";
		}
		
	}
}
